package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.Heading
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.ToolId
import kotlin.math.abs
import kotlin.math.max

class ApplyClueUseCase {

    fun headingFrom(state: ExpeditionState, fromIndex: Int): Heading {
        val dr = state.rowOf(state.coreIndex) - state.rowOf(fromIndex)
        val dc = state.colOf(state.coreIndex) - state.colOf(fromIndex)
        val north = dr < 0
        val south = dr > 0
        val east = dc > 0
        val west = dc < 0
        return when {
            north && east -> Heading.NORTH_EAST
            north && west -> Heading.NORTH_WEST
            south && east -> Heading.SOUTH_EAST
            south && west -> Heading.SOUTH_WEST
            north -> Heading.NORTH
            south -> Heading.SOUTH
            east -> Heading.EAST
            else -> Heading.WEST
        }
    }

    fun ringDistance(state: ExpeditionState, fromIndex: Int): Int = max(
        abs(state.rowOf(state.coreIndex) - state.rowOf(fromIndex)),
        abs(state.colOf(state.coreIndex) - state.colOf(fromIndex))
    )

    operator fun invoke(state: ExpeditionState, fromIndex: Int): ExpeditionState {
        val sharpened = state.preciseClues || state.hasTool(ToolId.CORE_SENSE)
        val matches = (state.rooms.indices).filter { index ->
            val room = state.rooms[index]
            if (room.collapsed) return@filter false
            if (room.revealed && room.kind != RoomKind.CORE) return@filter false
            if (sharpened) {
                ringAround(state, fromIndex, index) == ringDistance(state, fromIndex)
            } else {
                inHeading(state, fromIndex, index, headingFrom(state, fromIndex))
            }
        }.toSet()

        val previous = state.rooms.filter { it.candidate }.map { it.index }.toSet()
        val narrowed = if (previous.isEmpty()) matches else {
            val intersection = previous intersect matches
            if (intersection.isEmpty()) matches else intersection
        }
        val refined = if (state.hasTool(ToolId.CORE_SENSE) && narrowed.size > 1) {
            val tightest = narrowed.filter { ringAround(state, state.teamIndex, it) <= ringDistance(state, state.teamIndex) + 1 }
            if (tightest.isEmpty()) narrowed else tightest.toSet()
        } else {
            narrowed
        }

        val rooms = state.rooms.map { room -> room.copy(candidate = refined.contains(room.index)) }
        return state.copy(rooms = rooms, cluesRead = state.cluesRead + 1)
    }

    private fun ringAround(state: ExpeditionState, from: Int, target: Int): Int = max(
        abs(state.rowOf(target) - state.rowOf(from)),
        abs(state.colOf(target) - state.colOf(from))
    )

    private fun inHeading(state: ExpeditionState, from: Int, target: Int, heading: Heading): Boolean {
        val dr = state.rowOf(target) - state.rowOf(from)
        val dc = state.colOf(target) - state.colOf(from)
        return when (heading) {
            Heading.NORTH -> dr < 0
            Heading.SOUTH -> dr > 0
            Heading.EAST -> dc > 0
            Heading.WEST -> dc < 0
            Heading.NORTH_EAST -> dr < 0 && dc > 0
            Heading.NORTH_WEST -> dr < 0 && dc < 0
            Heading.SOUTH_EAST -> dr > 0 && dc > 0
            Heading.SOUTH_WEST -> dr > 0 && dc < 0
        }
    }
}
