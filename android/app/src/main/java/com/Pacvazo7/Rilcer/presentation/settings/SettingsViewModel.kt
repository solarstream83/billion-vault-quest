package com.Pacvazo7.Rilcer.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())

    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.update { current ->
                    current.copy(
                        cols = settings.cols,
                        preciseClues = settings.preciseClues,
                        startingSupplies = settings.startingSupplies,
                        reducedMotion = settings.reducedMotion
                    )
                }
            }
        }
    }

    fun chooseCols(cols: Int) {
        settingsRepository.setCols(cols)
    }

    fun choosePrecision(precise: Boolean) {
        settingsRepository.setPreciseClues(precise)
    }

    fun chooseSupplies(supplies: Int) {
        settingsRepository.setStartingSupplies(supplies)
    }

    fun toggleReducedMotion(reduced: Boolean) {
        settingsRepository.setReducedMotion(reduced)
    }

    fun askReset() {
        _state.update { it.copy(confirmResetVisible = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(confirmResetVisible = false) }
    }

    fun confirmReset() {
        progressRepository.reset()
        _state.update { it.copy(confirmResetVisible = false, resetDone = true) }
    }
}
