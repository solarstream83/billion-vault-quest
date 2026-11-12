package com.Pacvazo7.Rilcer.presentation.workshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.data.sample.SampleData
import com.Pacvazo7.Rilcer.domain.model.ToolId
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.usecase.UpgradeToolUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ToolWorkshopViewModel(
    private val progressRepository: ProgressRepository,
    private val upgradeTool: UpgradeToolUseCase
) : ViewModel() {

    val state: StateFlow<ToolWorkshopUiState> = progressRepository.progress
        .map { progress ->
            val rows = SampleData.tools.map { tool ->
                val level = progress.levelOf(tool)
                val cost = upgradeTool.costFor(level)
                ToolRowState(
                    tool = tool,
                    level = level,
                    cost = cost,
                    affordable = cost > 0 && progress.crystals >= cost,
                    mastered = level >= GameConfig.TOOL_MAX_LEVEL
                )
            }
            ToolWorkshopUiState(
                crystals = progress.crystals,
                rows = rows,
                allMastered = rows.all { it.mastered }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ToolWorkshopUiState())

    fun upgrade(tool: ToolId) {
        upgradeTool(tool)
    }
}
