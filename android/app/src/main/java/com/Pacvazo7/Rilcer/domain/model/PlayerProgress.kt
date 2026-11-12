package com.Pacvazo7.Rilcer.domain.model

data class PlayerProgress(
    val crystals: Int = 0,
    val depthRecord: Int = 0,
    val expeditions: Int = 0,
    val toolLevels: Map<ToolId, Int> = ToolId.entries.associateWith { 1 },
    val ownedArtifacts: Set<Int> = emptySet(),
    val objectiveIndex: Int = 0,
    val clearedRoomsBest: Int = 0
) {
    val activeObjective: ObjectiveId?
        get() = ObjectiveId.entries.getOrNull(objectiveIndex)

    fun levelOf(tool: ToolId): Int = toolLevels[tool] ?: 1
}
