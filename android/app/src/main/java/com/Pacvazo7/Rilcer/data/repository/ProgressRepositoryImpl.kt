package com.Pacvazo7.Rilcer.data.repository

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.data.local.PreferencesStorage
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.ObjectiveId
import com.Pacvazo7.Rilcer.domain.model.PlayerProgress
import com.Pacvazo7.Rilcer.domain.model.ToolId
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProgressRepositoryImpl(
    private val storage: PreferencesStorage
) : ProgressRepository {

    private val state = MutableStateFlow(load())

    override val progress: StateFlow<PlayerProgress> = state.asStateFlow()

    override fun addCrystals(amount: Int) {
        if (amount <= 0) return
        state.update { current ->
            val value = current.crystals + amount
            storage.writeInt(PreferencesStorage.KEY_CRYSTALS, value)
            current.copy(crystals = value)
        }
    }

    override fun spendCrystals(amount: Int): Boolean {
        if (amount <= 0) return true
        if (state.value.crystals < amount) return false
        state.update { current ->
            val value = current.crystals - amount
            storage.writeInt(PreferencesStorage.KEY_CRYSTALS, value)
            current.copy(crystals = value)
        }
        return true
    }

    override fun setToolLevel(tool: ToolId, level: Int) {
        storage.writeInt(PreferencesStorage.KEY_TOOL_PREFIX + tool.name, level)
        state.update { current -> current.copy(toolLevels = current.toolLevels + (tool to level)) }
    }

    override fun unlockArtifacts(ids: List<Int>) {
        if (ids.isEmpty()) return
        state.update { current ->
            val owned = current.ownedArtifacts + ids
            storage.writeIntSet(PreferencesStorage.KEY_ARTIFACTS, owned)
            current.copy(ownedArtifacts = owned)
        }
    }

    override fun completeRun(roomsCleared: Int, coreFound: Boolean) {
        state.update { current ->
            val expeditions = current.expeditions + 1
            val record = maxOf(current.depthRecord, roomsCleared)
            val best = maxOf(current.clearedRoomsBest, roomsCleared)
            storage.writeInt(PreferencesStorage.KEY_EXPEDITIONS, expeditions)
            storage.writeInt(PreferencesStorage.KEY_DEPTH_RECORD, record)
            storage.writeInt(PreferencesStorage.KEY_CLEARED_BEST, best)
            current.copy(
                expeditions = expeditions,
                depthRecord = record,
                clearedRoomsBest = best
            )
        }
    }

    override fun advanceObjective() {
        state.update { current ->
            val next = minOf(current.objectiveIndex + 1, ObjectiveId.entries.size)
            storage.writeInt(PreferencesStorage.KEY_OBJECTIVE_INDEX, next)
            current.copy(objectiveIndex = next)
        }
    }

    override fun completedSets(): List<ArtifactSetId> {
        val owned = state.value.ownedArtifacts
        return ArtifactSetId.entries.filterIndexed { setIndex, _ ->
            val first = setIndex * GameConfig.ARTIFACT_SET_SIZE
            (first until first + GameConfig.ARTIFACT_SET_SIZE).all { owned.contains(it) }
        }
    }

    override fun reset() {
        storage.writeInt(PreferencesStorage.KEY_CRYSTALS, 0)
        storage.writeInt(PreferencesStorage.KEY_DEPTH_RECORD, 0)
        storage.writeInt(PreferencesStorage.KEY_EXPEDITIONS, 0)
        storage.writeInt(PreferencesStorage.KEY_OBJECTIVE_INDEX, 0)
        storage.writeInt(PreferencesStorage.KEY_CLEARED_BEST, 0)
        storage.writeIntSet(PreferencesStorage.KEY_ARTIFACTS, emptySet())
        ToolId.entries.forEach { storage.writeInt(PreferencesStorage.KEY_TOOL_PREFIX + it.name, 1) }
        state.value = load()
    }

    private fun load(): PlayerProgress = PlayerProgress(
        crystals = storage.readInt(PreferencesStorage.KEY_CRYSTALS, 0),
        depthRecord = storage.readInt(PreferencesStorage.KEY_DEPTH_RECORD, 0),
        expeditions = storage.readInt(PreferencesStorage.KEY_EXPEDITIONS, 0),
        toolLevels = ToolId.entries.associateWith {
            storage.readInt(PreferencesStorage.KEY_TOOL_PREFIX + it.name, 1)
        },
        ownedArtifacts = storage.readIntSet(PreferencesStorage.KEY_ARTIFACTS),
        objectiveIndex = storage.readInt(PreferencesStorage.KEY_OBJECTIVE_INDEX, 0),
        clearedRoomsBest = storage.readInt(PreferencesStorage.KEY_CLEARED_BEST, 0)
    )
}
