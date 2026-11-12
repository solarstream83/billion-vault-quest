package com.Pacvazo7.Rilcer.presentation.exploration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.domain.model.Decision
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.repository.RunSessionRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import com.Pacvazo7.Rilcer.domain.usecase.FinishExpeditionUseCase
import com.Pacvazo7.Rilcer.domain.usecase.ResolveEncounterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ExplorationEvent {
    data object BackToMap : ExplorationEvent

    data class ShowSummary(val outcome: RunOutcome) : ExplorationEvent
}

class ExplorationTurnViewModel(
    private val sessionRepository: RunSessionRepository,
    private val settingsRepository: SettingsRepository,
    private val resolveEncounter: ResolveEncounterUseCase,
    private val finishExpedition: FinishExpeditionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExplorationTurnUiState())

    val state: StateFlow<ExplorationTurnUiState> = _state.asStateFlow()

    private val eventChannel = Channel<ExplorationEvent>(Channel.BUFFERED)

    val events: Flow<ExplorationEvent> = eventChannel.receiveAsFlow()

    fun prepare(roomIndex: Int) {
        val run = sessionRepository.expedition.value ?: return
        val room = run.rooms.getOrNull(roomIndex) ?: return
        val options = resolveEncounter.options(run)
        _state.value = ExplorationTurnUiState(
            roomIndex = roomIndex,
            kind = room.kind,
            trap = room.trap,
            options = options,
            outcome = null,
            supplies = run.supplies,
            crystals = run.crystals,
            fatiguedTool = options.any { it.blockedByFatigue },
            reducedMotion = settingsRepository.settings.value.reducedMotion
        )
    }

    fun decide(decision: Decision) {
        if (_state.value.resolved) return
        val run = sessionRepository.expedition.value ?: return
        val resolution = resolveEncounter(run, decision)
        sessionRepository.mutate { resolution.state }
        _state.update { current ->
            current.copy(
                outcome = resolution.outcome,
                options = emptyList(),
                suppliesDelta = resolution.state.supplies - current.supplies,
                crystalsDelta = resolution.state.crystals - current.crystals,
                supplies = resolution.state.supplies,
                crystals = resolution.state.crystals,
                runOutcome = resolution.state.outcome
            )
        }
        val finishing = resolution.state.outcome
        if (finishing != null) {
            val result = finishExpedition(resolution.state, finishing)
            sessionRepository.publishResult(result)
        }
    }

    fun proceed() {
        val finishing = _state.value.runOutcome
        if (finishing != null) {
            sessionRepository.clearRun()
            viewModelScope.launch { eventChannel.send(ExplorationEvent.ShowSummary(finishing)) }
        } else {
            viewModelScope.launch { eventChannel.send(ExplorationEvent.BackToMap) }
        }
    }
}
