package com.Pacvazo7.Rilcer.domain.model

data class ExpeditionState(
    val cols: Int,
    val rooms: List<Room>,
    val entranceIndex: Int,
    val coreIndex: Int,
    val teamIndex: Int,
    val previousIndex: Int,
    val selectedIndex: Int?,
    val supplies: Int,
    val startingSupplies: Int,
    val instability: Int,
    val turn: Int,
    val roomsCleared: Int,
    val crystals: Int,
    val artifacts: List<Int>,
    val team: List<Specialist>,
    val charges: Map<Specialist, Int>,
    val toolLevels: Map<ToolId, Int>,
    val seed: Long,
    val pendingIndex: Int?,
    val preciseClues: Boolean,
    val cluesRead: Int,
    val outcome: RunOutcome?
) {
    val rows: Int get() = cols

    fun rowOf(index: Int): Int = index / cols

    fun colOf(index: Int): Int = index % cols

    fun roomAt(index: Int): Room = rooms[index]

    fun isAdjacent(a: Int, b: Int): Boolean {
        val dr = rowOf(a) - rowOf(b)
        val dc = colOf(a) - colOf(b)
        return (dr == 0 && dc * dc == 1) || (dc == 0 && dr * dr == 1)
    }

    fun neighbours(index: Int): List<Int> {
        val result = ArrayList<Int>(4)
        val r = rowOf(index)
        val c = colOf(index)
        if (r > 0) result.add(index - cols)
        if (r < rows - 1) result.add(index + cols)
        if (c > 0) result.add(index - 1)
        if (c < cols - 1) result.add(index + 1)
        return result
    }

    fun canEnter(index: Int): Boolean =
        index in rooms.indices && !rooms[index].collapsed && isAdjacent(index, teamIndex)

    fun hasTool(tool: ToolId): Boolean = team.any { it.tool == tool }

    fun specialistFor(tool: ToolId): Specialist? = team.firstOrNull { it.tool == tool }

    fun chargesLeft(tool: ToolId): Int {
        val specialist = specialistFor(tool) ?: return 0
        return charges[specialist] ?: 0
    }

    fun candidateCount(): Int = rooms.count { it.candidate }

    fun exploredCount(): Int = rooms.count { it.revealed }
}
