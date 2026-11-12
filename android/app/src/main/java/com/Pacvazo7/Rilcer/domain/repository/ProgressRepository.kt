package com.Pacvazo7.Rilcer.domain.repository

import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.PlayerProgress
import com.Pacvazo7.Rilcer.domain.model.ToolId
import kotlinx.coroutines.flow.StateFlow

interface ProgressRepository {
    val progress: StateFlow<PlayerProgress>

    fun addCrystals(amount: Int)

    fun spendCrystals(amount: Int): Boolean

    fun setToolLevel(tool: ToolId, level: Int)

    fun unlockArtifacts(ids: List<Int>)

    fun completeRun(roomsCleared: Int, coreFound: Boolean)

    fun advanceObjective()

    fun completedSets(): List<ArtifactSetId>

    fun reset()
}
