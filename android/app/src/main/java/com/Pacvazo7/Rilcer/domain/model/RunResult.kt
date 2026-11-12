package com.Pacvazo7.Rilcer.domain.model

enum class RunOutcome { CORE_RECOVERED, TEAM_EXTRACTED, SUPPLIES_GONE }

data class RunResult(
    val outcome: RunOutcome,
    val roomsCleared: Int,
    val crystalsCarried: Int,
    val crystalsBanked: Int,
    val artifacts: List<Int>,
    val turns: Int,
    val newRecord: Boolean = false,
    val completedSets: List<ArtifactSetId> = emptyList(),
    val objective: ObjectiveProgress? = null,
    val objectiveJustCompleted: Boolean = false,
    val nextObjective: ObjectiveId? = null
)
