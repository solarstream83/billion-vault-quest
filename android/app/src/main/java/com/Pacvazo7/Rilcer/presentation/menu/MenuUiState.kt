package com.Pacvazo7.Rilcer.presentation.menu

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ObjectiveProgress

data class MenuUiState(
    val crystals: Int = 0,
    val depthRecord: Int = 0,
    val expeditions: Int = 0,
    val artifactsOwned: Int = 0,
    val artifactsTotal: Int = GameConfig.ARTIFACT_TOTAL,
    val objective: ObjectiveProgress? = null,
    val reducedMotion: Boolean = false
)
