package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import kotlin.random.Random

class AdvanceInstabilityUseCase {

    operator fun invoke(state: ExpeditionState, extra: Int): ExpeditionState {
        val before = state.instability
        val after = before + GameConfig.INSTABILITY_PER_TURN + extra
        var next = state.copy(instability = after)
        if (crossed(before, after, GameConfig.COLLAPSE_FIRST)) next = collapseOne(next)
        if (crossed(before, after, GameConfig.COLLAPSE_SECOND)) next = collapseOne(next)
        return next
    }

    private fun crossed(before: Int, after: Int, threshold: Int): Boolean =
        before < threshold && after >= threshold

    private fun collapseOne(state: ExpeditionState): ExpeditionState {
        val options = state.rooms.filter { room ->
            room.revealed &&
                !room.collapsed &&
                room.index != state.teamIndex &&
                room.index != state.entranceIndex &&
                room.index != state.coreIndex
        }
        if (options.isEmpty()) return state
        val random = Random(state.seed + state.turn * 7919L + state.instability)
        val victim = options[random.nextInt(options.size)]
        val rooms = state.rooms.map { room ->
            if (room.index == victim.index) room.copy(collapsed = true, candidate = false) else room
        }
        val candidate = state.copy(rooms = rooms)
        return if (routeHome(candidate)) candidate else state
    }

    private fun routeHome(state: ExpeditionState): Boolean {
        val seen = HashSet<Int>()
        val queue = ArrayDeque<Int>()
        queue.addLast(state.teamIndex)
        seen.add(state.teamIndex)
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == state.entranceIndex) return true
            for (next in state.neighbours(current)) {
                if (next in seen) continue
                if (state.rooms[next].collapsed) continue
                seen.add(next)
                queue.addLast(next)
            }
        }
        return false
    }
}
