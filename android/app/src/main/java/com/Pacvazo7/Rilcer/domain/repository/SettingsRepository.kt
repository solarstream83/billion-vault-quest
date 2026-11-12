package com.Pacvazo7.Rilcer.domain.repository

import com.Pacvazo7.Rilcer.domain.model.GameSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<GameSettings>

    fun setCols(cols: Int)

    fun setPreciseClues(precise: Boolean)

    fun setStartingSupplies(supplies: Int)

    fun setReducedMotion(reduced: Boolean)
}
