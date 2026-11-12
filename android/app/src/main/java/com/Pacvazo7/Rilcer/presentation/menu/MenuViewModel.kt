package com.Pacvazo7.Rilcer.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import com.Pacvazo7.Rilcer.domain.usecase.EvaluateObjectiveUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val evaluateObjective: EvaluateObjectiveUseCase
) : ViewModel() {

    val state: StateFlow<MenuUiState> = combine(
        progressRepository.progress,
        settingsRepository.settings
    ) { progress, settings ->
        MenuUiState(
            crystals = progress.crystals,
            depthRecord = progress.depthRecord,
            expeditions = progress.expeditions,
            artifactsOwned = progress.ownedArtifacts.size,
            objective = evaluateObjective.idleProgress(progress),
            reducedMotion = settings.reducedMotion
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, MenuUiState())
}
