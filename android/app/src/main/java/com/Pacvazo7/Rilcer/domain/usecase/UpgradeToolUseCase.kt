package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ToolId
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository

class UpgradeToolUseCase(
    private val progressRepository: ProgressRepository
) {

    fun costFor(level: Int): Int = when (level) {
        1 -> GameConfig.TOOL_LEVEL_TWO_COST
        2 -> GameConfig.TOOL_LEVEL_THREE_COST
        else -> 0
    }

    operator fun invoke(tool: ToolId): Boolean {
        val progress = progressRepository.progress.value
        val level = progress.levelOf(tool)
        if (level >= GameConfig.TOOL_MAX_LEVEL) return false
        val cost = costFor(level)
        if (!progressRepository.spendCrystals(cost)) return false
        progressRepository.setToolLevel(tool, level + 1)
        return true
    }
}
