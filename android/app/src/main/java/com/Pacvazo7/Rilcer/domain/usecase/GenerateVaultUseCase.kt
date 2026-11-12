package com.Pacvazo7.Rilcer.domain.usecase

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.Room
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.TrapKind
import kotlin.math.abs
import kotlin.random.Random

data class VaultLayout(
    val rooms: List<Room>,
    val entranceIndex: Int,
    val coreIndex: Int,
    val seed: Long
)

class GenerateVaultUseCase {

    operator fun invoke(cols: Int, seed: Long, supplyBudget: Int): VaultLayout {
        val entrance = (cols - 1) * cols + cols / 2
        var attempt = 0
        while (attempt < GameConfig.GENERATION_ATTEMPTS) {
            val random = Random(seed + attempt * 104729L)
            val layout = build(cols, entrance, random, seed)
            if (solvable(layout, cols, supplyBudget)) return layout
            attempt++
        }
        return fallback(cols, entrance, seed)
    }

    private fun build(cols: Int, entrance: Int, random: Random, seed: Long): VaultLayout {
        val total = cols * cols
        val kinds = MutableList(total) { RoomKind.EMPTY }
        val traps = arrayOfNulls<TrapKind>(total)

        val free = (0 until total).filter { it != entrance }.shuffled(random).toMutableList()
        val coreCandidates = free.filter { it / cols != cols - 1 }
        val core = coreCandidates.firstOrNull() ?: free.first()
        free.remove(core)
        kinds[core] = RoomKind.CORE

        val clueCount = if (cols >= GameConfig.GRID_DEEP) 7 else 5
        val trapCount = if (cols >= GameConfig.GRID_DEEP) 7 else 5
        val cacheCount = if (cols >= GameConfig.GRID_DEEP) 6 else 4

        repeat(clueCount) { if (free.isNotEmpty()) kinds[free.removeAt(0)] = RoomKind.CLUE }
        repeat(trapCount) {
            if (free.isNotEmpty()) {
                val index = free.removeAt(0)
                kinds[index] = RoomKind.TRAP
                traps[index] = if (random.nextBoolean()) TrapKind.COLLAPSE else TrapKind.SEAL
            }
        }
        repeat(cacheCount) { if (free.isNotEmpty()) kinds[free.removeAt(0)] = RoomKind.CACHE }

        val rooms = (0 until total).map { index ->
            Room(
                index = index,
                kind = kinds[index],
                trap = traps[index],
                revealed = index == entrance
            )
        }
        return VaultLayout(rooms, entrance, core, seed)
    }

    private fun solvable(layout: VaultLayout, cols: Int, supplyBudget: Int): Boolean {
        val steps = manhattan(layout.entranceIndex, layout.coreIndex, cols)
        if (steps > supplyBudget - 2) return false
        if (steps < 2) return false
        val onPath = layout.rooms.count { room ->
            room.kind == RoomKind.CLUE && withinCorridor(room.index, layout.entranceIndex, layout.coreIndex, cols)
        }
        return onPath >= 2
    }

    private fun withinCorridor(index: Int, from: Int, to: Int, cols: Int): Boolean {
        val r = index / cols
        val c = index % cols
        val r1 = minOf(from / cols, to / cols)
        val r2 = maxOf(from / cols, to / cols)
        val c1 = minOf(from % cols, to % cols)
        val c2 = maxOf(from % cols, to % cols)
        return r in (r1 - 1)..(r2 + 1) && c in (c1 - 1)..(c2 + 1)
    }

    private fun manhattan(a: Int, b: Int, cols: Int): Int =
        abs(a / cols - b / cols) + abs(a % cols - b % cols)

    private fun fallback(cols: Int, entrance: Int, seed: Long): VaultLayout {
        val total = cols * cols
        val core = cols / 2
        val kinds = MutableList(total) { RoomKind.EMPTY }
        val traps = arrayOfNulls<TrapKind>(total)
        kinds[core] = RoomKind.CORE
        val column = entrance % cols
        var placed = 0
        for (row in cols - 2 downTo 1) {
            val index = row * cols + column
            if (index == core) continue
            kinds[index] = if (placed % 2 == 0) RoomKind.CLUE else RoomKind.CACHE
            placed++
        }
        for (row in 1 until cols - 1) {
            val index = row * cols + ((column + 1) % cols)
            if (index == core || index == entrance) continue
            kinds[index] = RoomKind.TRAP
            traps[index] = if (row % 2 == 0) TrapKind.COLLAPSE else TrapKind.SEAL
        }
        val rooms = (0 until total).map { index ->
            Room(
                index = index,
                kind = kinds[index],
                trap = traps[index],
                revealed = index == entrance
            )
        }
        return VaultLayout(rooms, entrance, core, seed)
    }
}
