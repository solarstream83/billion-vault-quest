package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.ObjectiveId
import com.Pacvazo7.Rilcer.domain.model.ObjectiveProgress
import com.Pacvazo7.Rilcer.domain.model.PlayerProgress
import com.Pacvazo7.Rilcer.domain.model.RunOutcome

class EvaluateObjectiveUseCase {

    operator fun invoke(
        progress: PlayerProgress,
        outcome: RunOutcome,
        roomsCleared: Int,
        artifactsThisRun: Int,
        completedSets: List<ArtifactSetId>,
        deepVault: Boolean
    ): ObjectiveProgress? {
        val objective = progress.activeObjective ?: return null
        val current = when (objective) {
            ObjectiveId.CLEAR_EIGHT_ROOMS -> roomsCleared
            ObjectiveId.RECOVER_TWO_ARTIFACTS -> artifactsThisRun
            ObjectiveId.UPGRADE_ANY_TOOL -> if (progress.toolLevels.values.any { it >= 2 }) 1 else 0
            ObjectiveId.COMPLETE_ONE_SET -> if (completedSets.isNotEmpty()) 1 else 0
            ObjectiveId.RECOVER_THE_CORE -> if (outcome == RunOutcome.CORE_RECOVERED) 1 else 0
            ObjectiveId.RECOVER_CORE_DEEP ->
                if (outcome == RunOutcome.CORE_RECOVERED && deepVault) 1 else 0
        }
        val capped = minOf(current, objective.target)
        return ObjectiveProgress(
            id = objective,
            current = capped,
            target = objective.target,
            completed = capped >= objective.target
        )
    }

    fun idleProgress(progress: PlayerProgress): ObjectiveProgress? {
        val objective = progress.activeObjective ?: return null
        val current = when (objective) {
            ObjectiveId.CLEAR_EIGHT_ROOMS -> minOf(progress.clearedRoomsBest, objective.target)
            ObjectiveId.UPGRADE_ANY_TOOL -> if (progress.toolLevels.values.any { it >= 2 }) 1 else 0
            ObjectiveId.COMPLETE_ONE_SET -> completedSetCount(progress)
            else -> 0
        }
        return ObjectiveProgress(
            id = objective,
            current = minOf(current, objective.target),
            target = objective.target,
            completed = false
        )
    }

    private fun completedSetCount(progress: PlayerProgress): Int {
        var count = 0
        ArtifactSetId.entries.forEachIndexed { setIndex, _ ->
            val first = setIndex * GameConfig.ARTIFACT_SET_SIZE
            val range = first until first + GameConfig.ARTIFACT_SET_SIZE
            if (range.all { progress.ownedArtifacts.contains(it) }) count++
        }
        return count
    }
}
