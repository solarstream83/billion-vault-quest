package com.Pacvazo7.Rilcer.domain.model

enum class ToolId { SCANNER, BRACE, LOCKPICK, PACK, CORE_SENSE }

enum class Specialist(val tool: ToolId) {
    SURVEYOR(ToolId.SCANNER),
    ENGINEER(ToolId.BRACE),
    LOCKSMITH(ToolId.LOCKPICK),
    PORTER(ToolId.PACK),
    GEOLOGIST(ToolId.CORE_SENSE)
}

fun ToolId.answers(trap: TrapKind): Boolean = when (this) {
    ToolId.BRACE -> trap == TrapKind.COLLAPSE
    ToolId.LOCKPICK -> trap == TrapKind.SEAL
    else -> false
}
