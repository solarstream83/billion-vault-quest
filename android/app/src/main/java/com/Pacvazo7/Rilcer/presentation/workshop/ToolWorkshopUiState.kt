package com.Pacvazo7.Rilcer.presentation.workshop

import com.Pacvazo7.Rilcer.domain.model.ToolId

data class ToolRowState(
    val tool: ToolId,
    val level: Int,
    val cost: Int,
    val affordable: Boolean,
    val mastered: Boolean
)

data class ToolWorkshopUiState(
    val crystals: Int = 0,
    val rows: List<ToolRowState> = emptyList(),
    val allMastered: Boolean = false
)
