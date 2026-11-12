package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.GameSettings
import com.Pacvazo7.Rilcer.domain.model.PlayerProgress
import com.Pacvazo7.Rilcer.domain.model.Specialist
import com.Pacvazo7.Rilcer.domain.model.ToolId

class StartExpeditionUseCase(
    private val generateVault: GenerateVaultUseCase,
    private val applyClue: ApplyClueUseCase
) {

    fun startingSupplies(
        settings: GameSettings,
        team: List<Specialist>,
        completedSets: List<ArtifactSetId>
    ): Int {
        var supplies = settings.startingSupplies
        if (team.contains(Specialist.PORTER)) supplies += GameConfig.PORTER_SUPPLY_BONUS
        if (completedSets.contains(ArtifactSetId.MINERAL_RELICS)) {
            supplies += GameConfig.SET_SUPPLY_BONUS
        }
        return supplies
    }

    fun chargesFor(
        team: List<Specialist>,
        progress: PlayerProgress,
        completedSets: List<ArtifactSetId>
    ): Map<Specialist, Int> {
        val setBonus = if (completedSets.contains(ArtifactSetId.BRASS_MECHANISMS)) {
            GameConfig.CHARGES_MASTER_BONUS
        } else {
            0
        }
        return team.associateWith { specialist ->
            val mastered = progress.levelOf(specialist.tool) >= GameConfig.TOOL_MAX_LEVEL
            GameConfig.CHARGES_BASE + setBonus + if (mastered) GameConfig.CHARGES_MASTER_BONUS else 0
        }
    }

    operator fun invoke(
        team: List<Specialist>,
        settings: GameSettings,
        progress: PlayerProgress,
        completedSets: List<ArtifactSetId>,
        attempt: Int
    ): ExpeditionState {
        val supplies = startingSupplies(settings, team, completedSets)
        val seed = (progress.expeditions + 1) * 7919L xor (attempt + 1) * 104729L
        val layout = generateVault(settings.cols, seed, supplies)
        val toolLevels = ToolId.entries.associateWith { progress.levelOf(it) }
        val base = ExpeditionState(
            cols = settings.cols,
            rooms = layout.rooms,
            entranceIndex = layout.entranceIndex,
            coreIndex = layout.coreIndex,
            teamIndex = layout.entranceIndex,
            previousIndex = layout.entranceIndex,
            selectedIndex = null,
            supplies = supplies,
            startingSupplies = supplies,
            instability = 0,
            turn = 1,
            roomsCleared = 0,
            crystals = 0,
            artifacts = emptyList(),
            team = team,
            charges = chargesFor(team, progress, completedSets),
            toolLevels = toolLevels,
            seed = seed,
            pendingIndex = null,
            preciseClues = settings.preciseClues,
            cluesRead = 0,
            outcome = null
        )
        return if (completedSets.contains(ArtifactSetId.CORE_FRAGMENTS)) {
            applyClue(base, layout.entranceIndex)
        } else {
            base
        }
    }
}
