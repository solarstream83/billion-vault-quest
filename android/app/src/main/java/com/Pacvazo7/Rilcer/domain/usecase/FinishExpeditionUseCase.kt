package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.ObjectiveId
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.model.RunResult
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository

class FinishExpeditionUseCase(
    private val progressRepository: ProgressRepository,
    private val evaluateObjective: EvaluateObjectiveUseCase
) {

    operator fun invoke(state: ExpeditionState, outcome: RunOutcome): RunResult {
        val before = progressRepository.progress.value
        val setsBefore = progressRepository.completedSets()
        val banked = if (outcome == RunOutcome.SUPPLIES_GONE) state.crystals / 2 else state.crystals

        progressRepository.addCrystals(banked)
        progressRepository.unlockArtifacts(state.artifacts)
        progressRepository.completeRun(
            roomsCleared = state.roomsCleared,
            coreFound = outcome == RunOutcome.CORE_RECOVERED
        )

        val setsAfter = progressRepository.completedSets()
        val freshSets = setsAfter.filter { it !in setsBefore }

        val objective = evaluateObjective(
            progress = progressRepository.progress.value,
            outcome = outcome,
            roomsCleared = state.roomsCleared,
            artifactsThisRun = state.artifacts.size,
            completedSets = setsAfter,
            deepVault = state.cols >= GameConfig.GRID_DEEP
        )
        var justCompleted = false
        var nextObjective: ObjectiveId? = objective?.id
        if (objective != null && objective.completed) {
            justCompleted = true
            progressRepository.addCrystals(GameConfig.OBJECTIVE_REWARD)
            progressRepository.advanceObjective()
            nextObjective = progressRepository.progress.value.activeObjective
        }

        return RunResult(
            outcome = outcome,
            roomsCleared = state.roomsCleared,
            crystalsCarried = state.crystals,
            crystalsBanked = banked + if (justCompleted) GameConfig.OBJECTIVE_REWARD else 0,
            artifacts = state.artifacts,
            turns = state.turn,
            newRecord = state.roomsCleared > before.depthRecord,
            completedSets = freshSets,
            objective = objective,
            objectiveJustCompleted = justCompleted,
            nextObjective = nextObjective
        )
    }
}
