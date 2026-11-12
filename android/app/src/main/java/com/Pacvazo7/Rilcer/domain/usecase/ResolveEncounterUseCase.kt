package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.Decision
import com.Pacvazo7.Rilcer.domain.model.DecisionOption
import com.Pacvazo7.Rilcer.domain.model.EncounterOutcome
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.OutcomeKind
import com.Pacvazo7.Rilcer.domain.model.Room
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.model.ToolId
import com.Pacvazo7.Rilcer.domain.model.answers
import kotlin.random.Random

data class EncounterResolution(
    val state: ExpeditionState,
    val outcome: EncounterOutcome
)

class ResolveEncounterUseCase(
    private val applyClue: ApplyClueUseCase
) {

    fun options(state: ExpeditionState): List<DecisionOption> {
        val pending = state.pendingIndex ?: return emptyList()
        val room = state.rooms.getOrNull(pending) ?: return emptyList()
        val result = ArrayList<DecisionOption>(3)
        val tool = toolFor(room)
        if (tool != null) {
            val carried = state.hasTool(tool)
            val charges = state.chargesLeft(tool)
            result.add(
                DecisionOption(
                    decision = Decision.USE_TOOL,
                    enabled = carried && charges > 0,
                    cost = 0,
                    tool = tool,
                    blockedByFatigue = carried && charges <= 0
                )
            )
        }
        result.add(
            DecisionOption(
                decision = Decision.PUSH_ON,
                enabled = true,
                cost = if (room.kind == RoomKind.TRAP) GameConfig.PUSH_ON_TRAP_COST else 0
            )
        )
        if (room.kind != RoomKind.CORE) {
            result.add(
                DecisionOption(
                    decision = Decision.FALL_BACK,
                    enabled = state.previousIndex != state.teamIndex,
                    cost = GameConfig.FALL_BACK_COST
                )
            )
        }
        return result
    }

    private fun toolFor(room: Room): ToolId? {
        val trap = room.trap
        return when (room.kind) {
            RoomKind.TRAP -> when {
                trap == null -> null
                ToolId.BRACE.answers(trap) -> ToolId.BRACE
                else -> ToolId.LOCKPICK
            }
            RoomKind.CACHE -> ToolId.LOCKPICK
            RoomKind.CLUE -> ToolId.SCANNER
            else -> null
        }
    }

    operator fun invoke(state: ExpeditionState, decision: Decision): EncounterResolution {
        val index = state.pendingIndex
            ?: return EncounterResolution(state, EncounterOutcome(OutcomeKind.EMPTY_GALLERY))
        val room = state.rooms[index]
        val random = Random(state.seed + index * 31L + state.turn)

        if (decision == Decision.FALL_BACK) {
            val moved = state.copy(
                supplies = state.supplies - GameConfig.FALL_BACK_COST,
                teamIndex = state.previousIndex,
                selectedIndex = null,
                pendingIndex = null
            )
            return EncounterResolution(
                finishIfDrained(moved),
                EncounterOutcome(OutcomeKind.STEPPED_BACK, GameConfig.FALL_BACK_COST)
            )
        }

        val usedTool = if (decision == Decision.USE_TOOL) toolFor(room) else null
        var next = state.copy(
            rooms = state.rooms.map {
                if (it.index == index) it.copy(revealed = true, candidate = false) else it
            },
            roomsCleared = state.roomsCleared + 1,
            pendingIndex = null,
            selectedIndex = null
        )
        if (usedTool != null) {
            val specialist = state.specialistFor(usedTool)
            if (specialist != null) {
                val left = (next.charges[specialist] ?: 0) - 1
                next = next.copy(charges = next.charges + (specialist to maxOf(0, left)))
            }
        }

        val outcome: EncounterOutcome = when (room.kind) {
            RoomKind.TRAP -> {
                if (usedTool != null) {
                    EncounterOutcome(
                        kind = if (usedTool == ToolId.BRACE) OutcomeKind.BRACE_HOLDS else OutcomeKind.SEAL_PICKED,
                        toolUsed = usedTool
                    )
                } else {
                    next = next.copy(
                        supplies = next.supplies - GameConfig.PUSH_ON_TRAP_COST,
                        instability = next.instability + GameConfig.INSTABILITY_PUSH_ON
                    )
                    EncounterOutcome(OutcomeKind.SUPPLIES_SPILLED, GameConfig.PUSH_ON_TRAP_COST)
                }
            }

            RoomKind.CACHE -> {
                val full = GameConfig.CACHE_CRYSTALS_BASE + random.nextInt(GameConfig.CACHE_CRYSTALS_SPREAD)
                if (usedTool != null) {
                    val artifact = if (random.nextInt(GameConfig.CACHE_ARTIFACT_EVERY) == 0) {
                        pickArtifact(next, random)
                    } else {
                        null
                    }
                    next = next.copy(
                        crystals = next.crystals + full,
                        artifacts = if (artifact != null) next.artifacts + artifact else next.artifacts,
                        rooms = next.rooms.map { if (it.index == index) it.copy(looted = true) else it }
                    )
                    EncounterOutcome(
                        kind = OutcomeKind.CRYSTALS_POCKETED,
                        amount = full,
                        artifactGained = artifact,
                        toolUsed = usedTool
                    )
                } else {
                    val half = full / 2
                    next = next.copy(
                        crystals = next.crystals + half,
                        rooms = next.rooms.map { if (it.index == index) it.copy(looted = true) else it }
                    )
                    EncounterOutcome(OutcomeKind.CRYSTALS_SCRAPED, half)
                }
            }

            RoomKind.CLUE -> {
                val heading = applyClue.headingFrom(next, index)
                val distance = applyClue.ringDistance(next, index)
                next = applyClue(next, index)
                if (usedTool != null) {
                    val peek = next.neighbours(index)
                        .firstOrNull { !next.rooms[it].revealed && !next.rooms[it].collapsed }
                    if (peek != null) {
                        next = next.copy(
                            rooms = next.rooms.map {
                                if (it.index == peek) it.copy(revealed = true) else it
                            }
                        )
                    }
                    EncounterOutcome(OutcomeKind.SCANNER_PEEK, distance, heading, toolUsed = usedTool)
                } else if (next.preciseClues || next.hasTool(ToolId.CORE_SENSE)) {
                    EncounterOutcome(OutcomeKind.SURVEY_PRECISE, distance, heading)
                } else {
                    EncounterOutcome(OutcomeKind.SURVEY_VAGUE, distance, heading)
                }
            }

            RoomKind.CORE -> {
                val bonus = GameConfig.CORE_BONUS_BASE + GameConfig.CORE_BONUS_PER_ROOM * next.roomsCleared
                val artifact = pickArtifact(next, random)
                next = next.copy(
                    crystals = next.crystals + bonus,
                    artifacts = if (artifact != null) next.artifacts + artifact else next.artifacts,
                    outcome = RunOutcome.CORE_RECOVERED
                )
                EncounterOutcome(OutcomeKind.CORE_RECOVERED, bonus, artifactGained = artifact)
            }

            RoomKind.EMPTY -> EncounterOutcome(OutcomeKind.EMPTY_GALLERY)
        }

        return EncounterResolution(finishIfDrained(next), outcome)
    }

    private fun pickArtifact(state: ExpeditionState, random: Random): Int? {
        val available = (0 until GameConfig.ARTIFACT_TOTAL).filter { it !in state.artifacts }
        if (available.isEmpty()) return null
        return available[random.nextInt(available.size)]
    }

    private fun finishIfDrained(state: ExpeditionState): ExpeditionState =
        if (state.outcome == null && state.supplies <= 0) {
            state.copy(supplies = 0, outcome = RunOutcome.SUPPLIES_GONE)
        } else {
            state
        }
}
