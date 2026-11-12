package com.Pacvazo7.Rilcer.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
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

sealed interface GameOverEvent {
    data object EnterVault : GameOverEvent

    data object PickTeam : GameOverEvent
}

class GameOverViewModel(
    private val sessionRepository: RunSessionRepository,
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val startExpedition: StartExpeditionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())

    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    private val eventChannel = Channel<GameOverEvent>(Channel.BUFFERED)

    val events: Flow<GameOverEvent> = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            sessionRepository.lastResult.collect { result ->
                _state.update { current ->
                    current.copy(
                        result = result,
                        outcome = result?.outcome ?: current.outcome,
                        crystalBalance = progressRepository.progress.value.crystals,
                        reducedMotion = settingsRepository.settings.value.reducedMotion,
                        replayReady = sessionRepository.lastTeam.value.size >= GameConfig.TEAM_SIZE
                    )
                }
            }
        }
    }

    fun bind(outcome: RunOutcome) {
        _state.update { current ->
            current.copy(
                outcome = current.result?.outcome ?: outcome,
                crystalBalance = progressRepository.progress.value.crystals,
                replayReady = sessionRepository.lastTeam.value.size >= GameConfig.TEAM_SIZE
            )
        }
    }

    fun playAgain() {
        val team = sessionRepository.lastTeam.value
        if (team.size < GameConfig.TEAM_SIZE) {
            viewModelScope.launch { eventChannel.send(GameOverEvent.PickTeam) }
            return
        }
        val fresh = startExpedition(
            team = team,
            settings = settingsRepository.settings.value,
            progress = progressRepository.progress.value,
            completedSets = progressRepository.completedSets(),
            attempt = progressRepository.progress.value.expeditions
        )
        sessionRepository.begin(fresh)
        viewModelScope.launch { eventChannel.send(GameOverEvent.EnterVault) }
    }
}
