package com.Pacvazo7.Rilcer.presentation.teamsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.data.sample.SampleData
import com.Pacvazo7.Rilcer.domain.model.Specialist
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.repository.RunSessionRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import com.Pacvazo7.Rilcer.domain.usecase.StartExpeditionUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface TeamSetupEvent {
    data object EnterVault : TeamSetupEvent
}

class TeamSetupViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val sessionRepository: RunSessionRepository,
    private val startExpedition: StartExpeditionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TeamSetupUiState())

    val state: StateFlow<TeamSetupUiState> = _state.asStateFlow()

    private val eventChannel = Channel<TeamSetupEvent>(Channel.BUFFERED)

    val events: Flow<TeamSetupEvent> = eventChannel.receiveAsFlow()

    init {
        val remembered = sessionRepository.lastTeam.value
        val slots = List(GameConfig.TEAM_SIZE) { index -> remembered.getOrNull(index) }
        _state.value = TeamSetupUiState(
            slots = slots,
            roster = SampleData.specialists,
            setBonuses = progressRepository.completedSets(),
            reducedMotion = settingsRepository.settings.value.reducedMotion
        )
        refreshPreview()
    }

    fun openSlot(index: Int) {
        _state.update { current ->
            current.copy(openSlot = if (current.openSlot == index) null else index)
        }
    }

    fun assign(specialist: Specialist) {
        _state.update { current ->
            val target = current.openSlot ?: return@update current
            if (current.slots.withIndex().any { it.index != target && it.value == specialist }) {
                return@update current
            }
            val slots = current.slots.toMutableList()
            slots[target] = specialist
            current.copy(slots = slots, openSlot = null)
        }
        refreshPreview()
    }

    fun clearSlot(index: Int) {
        _state.update { current ->
            val slots = current.slots.toMutableList()
            slots[index] = null
            current.copy(slots = slots, openSlot = index)
        }
        refreshPreview()
    }

    fun beginExpedition() {
        val team = _state.value.assigned
        if (team.size < GameConfig.TEAM_SIZE) return
        val expedition = startExpedition(
            team = team,
            settings = settingsRepository.settings.value,
            progress = progressRepository.progress.value,
            completedSets = progressRepository.completedSets(),
            attempt = 0
        )
        sessionRepository.begin(expedition)
        sessionRepository.rememberTeam(team)
        viewModelScope.launch { eventChannel.send(TeamSetupEvent.EnterVault) }
    }

    private fun refreshPreview() {
        _state.update { current ->
            current.copy(
                suppliesPreview = startExpedition.startingSupplies(
                    settings = settingsRepository.settings.value,
                    team = current.assigned,
                    completedSets = current.setBonuses
                )
            )
        }
    }
}
