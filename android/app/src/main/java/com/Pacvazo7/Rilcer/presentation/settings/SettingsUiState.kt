package com.Pacvazo7.Rilcer.presentation.settings

import com.Pacvazo7.Rilcer.core.config.GameConfig

data class SettingsUiState(
    val cols: Int = GameConfig.GRID_STANDARD,
    val preciseClues: Boolean = true,
    val startingSupplies: Int = GameConfig.DEFAULT_SUPPLIES,
    val reducedMotion: Boolean = false,
    val confirmResetVisible: Boolean = false,
    val resetDone: Boolean = false
)
