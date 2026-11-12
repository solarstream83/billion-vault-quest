package com.Pacvazo7.Rilcer.domain.model

enum class RoomKind { EMPTY, CLUE, TRAP, CACHE, CORE }

enum class TrapKind { COLLAPSE, SEAL }

data class Room(
    val index: Int,
    val kind: RoomKind,
    val trap: TrapKind? = null,
    val revealed: Boolean = false,
    val collapsed: Boolean = false,
    val candidate: Boolean = false,
    val looted: Boolean = false
)
