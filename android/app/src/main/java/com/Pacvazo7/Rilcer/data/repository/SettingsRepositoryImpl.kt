package com.Pacvazo7.Rilcer.data.repository

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.data.local.PreferencesStorage
import com.Pacvazo7.Rilcer.domain.model.GameSettings
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsRepositoryImpl(
    private val storage: PreferencesStorage
) : SettingsRepository {

    private val state = MutableStateFlow(
        GameSettings(
            cols = storage.readInt(PreferencesStorage.KEY_COLS, GameConfig.GRID_STANDARD),
            preciseClues = storage.readBoolean(PreferencesStorage.KEY_PRECISE, true),
            startingSupplies = storage.readInt(
                PreferencesStorage.KEY_SUPPLIES,
                GameConfig.DEFAULT_SUPPLIES
            ),
            reducedMotion = storage.readBoolean(PreferencesStorage.KEY_REDUCED_MOTION, false)
        )
    )

    override val settings: StateFlow<GameSettings> = state.asStateFlow()

    override fun setCols(cols: Int) {
        storage.writeInt(PreferencesStorage.KEY_COLS, cols)
        state.value = state.value.copy(cols = cols)
    }

    override fun setPreciseClues(precise: Boolean) {
        storage.writeBoolean(PreferencesStorage.KEY_PRECISE, precise)
        state.value = state.value.copy(preciseClues = precise)
    }

    override fun setStartingSupplies(supplies: Int) {
        storage.writeInt(PreferencesStorage.KEY_SUPPLIES, supplies)
        state.value = state.value.copy(startingSupplies = supplies)
    }

    override fun setReducedMotion(reduced: Boolean) {
        storage.writeBoolean(PreferencesStorage.KEY_REDUCED_MOTION, reduced)
        state.value = state.value.copy(reducedMotion = reduced)
    }
}
