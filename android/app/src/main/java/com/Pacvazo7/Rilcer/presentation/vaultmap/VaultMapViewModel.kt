package com.Pacvazo7.Rilcer.presentation.vaultmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.repository.RunSessionRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import com.Pacvazo7.Rilcer.domain.usecase.AdvanceInstabilityUseCase
import com.Pacvazo7.Rilcer.domain.usecase.FinishExpeditionUseCase
import com.Pacvazo7.Rilcer.domain.usecase.StartExpeditionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface VaultMapEvent {
    data class OpenRoom(val index: Int) : VaultMapEvent

    data class ShowSummary(val outcome: RunOutcome) : VaultMapEvent

    data object LeaveToMenu : VaultMapEvent
}

class VaultMapViewModel(
    private val sessionRepository: RunSessionRepository,
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository,
    private val advanceInstability: AdvanceInstabilityUseCase,
    private val finishExpedition: FinishExpeditionUseCase,
    private val startExpedition: StartExpeditionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(VaultMapUiState())

    val state: StateFlow<VaultMapUiState> = _state.asStateFlow()

    private val eventChannel = Channel<VaultMapEvent>(Channel.BUFFERED)

    val events: Flow<VaultMapEvent> = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            sessionRepository.expedition.collect { run ->
                _state.update { current ->
                    current.copy(
                        expedition = run,
                        reducedMotion = settingsRepository.settings.value.reducedMotion
                    )
                }
            }
        }
    }

    fun select(index: Int) {
        val run = _state.value.expedition ?: return
        if (!run.canEnter(index)) return
        sessionRepository.mutate { current ->
            current.copy(selectedIndex = if (current.selectedIndex == index) null else index)
        }
    }

    fun go() {
        val run = _state.value.expedition ?: return
        val target = run.selectedIndex ?: return
        if (!run.canEnter(target)) return
        val room = run.rooms[target]
        var moved = run.copy(
            previousIndex = run.teamIndex,
            teamIndex = target,
            supplies = run.supplies - GameConfig.MOVE_COST,
            turn = run.turn + 1,
            selectedIndex = null,
            pendingIndex = if (room.revealed) null else target
        )
        moved = advanceInstability(moved, 0)
        if (moved.supplies <= 0) {
            sessionRepository.mutate { moved.copy(supplies = 0, pendingIndex = null) }
            endRun(moved.copy(supplies = 0), RunOutcome.SUPPLIES_GONE)
            return
        }
        sessionRepository.mutate { moved }
        if (!room.revealed) {
            viewModelScope.launch { eventChannel.send(VaultMapEvent.OpenRoom(target)) }
        }
    }

    fun fallBack() {
        val run = _state.value.expedition ?: return
        if (!_state.value.canFallBack) return
        var moved = run.copy(
            teamIndex = run.previousIndex,
            previousIndex = run.teamIndex,
            supplies = run.supplies - GameConfig.FALL_BACK_COST,
            turn = run.turn + 1,
            selectedIndex = null,
            pendingIndex = null
        )
        moved = advanceInstability(moved, 0)
        if (moved.supplies <= 0) {
            endRun(moved.copy(supplies = 0), RunOutcome.SUPPLIES_GONE)
            return
        }
        sessionRepository.mutate { moved }
    }

    fun extract() {
        val run = _state.value.expedition ?: return
        endRun(run, RunOutcome.TEAM_EXTRACTED)
    }

    fun showPause() {
        _state.update { it.copy(pauseVisible = true) }
    }

    fun hidePause() {
        _state.update { it.copy(pauseVisible = false) }
    }

    fun askAbandon() {
        _state.update { it.copy(abandonVisible = true, pauseVisible = false) }
    }

    fun dismissAbandon() {
        _state.update { it.copy(abandonVisible = false) }
    }

    fun abandon() {
        _state.update { it.copy(abandonVisible = false, pauseVisible = false) }
        sessionRepository.clearRun()
        viewModelScope.launch { eventChannel.send(VaultMapEvent.LeaveToMenu) }
    }

    fun restart() {
        val run = _state.value.expedition ?: return
        _state.update { it.copy(pauseVisible = false, abandonVisible = false) }
        val fresh = startExpedition(
            team = run.team,
            settings = settingsRepository.settings.value,
            progress = progressRepository.progress.value,
            completedSets = progressRepository.completedSets(),
            attempt = run.turn + run.instability + 1
        )
        sessionRepository.begin(fresh)
    }

    fun pause() {
        _state.update { it.copy(pauseVisible = false, abandonVisible = false) }
    }

    private fun endRun(run: ExpeditionState, outcome: RunOutcome) {
        val result = finishExpedition(run, outcome)
        sessionRepository.publishResult(result)
        sessionRepository.clearRun()
        viewModelScope.launch { eventChannel.send(VaultMapEvent.ShowSummary(outcome)) }
    }
}
