package com.Pacvazo7.Rilcer.domain.model

import com.Pacvazo7.Rilcer.core.config.GameConfig

data class GameSettings(
    val cols: Int = GameConfig.GRID_STANDARD,
    val preciseClues: Boolean = true,
    val startingSupplies: Int = GameConfig.DEFAULT_SUPPLIES,
    val reducedMotion: Boolean = false
) {
    val deepVault: Boolean get() = cols >= GameConfig.GRID_DEEP
}
