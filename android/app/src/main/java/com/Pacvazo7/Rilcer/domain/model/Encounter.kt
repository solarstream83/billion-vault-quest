package com.Pacvazo7.Rilcer.domain.model

enum class Decision { USE_TOOL, PUSH_ON, FALL_BACK }

enum class OutcomeKind {
    BRACE_HOLDS,
    SEAL_PICKED,
    SUPPLIES_SPILLED,
    CRYSTALS_POCKETED,
    CRYSTALS_SCRAPED,
    SURVEY_PRECISE,
    SURVEY_VAGUE,
    SCANNER_PEEK,
    EMPTY_GALLERY,
    CORE_RECOVERED,
    STEPPED_BACK
}

enum class Heading { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST }

data class EncounterOutcome(
    val kind: OutcomeKind,
    val amount: Int = 0,
    val heading: Heading? = null,
    val artifactGained: Int? = null,
    val toolUsed: ToolId? = null
)

data class DecisionOption(
    val decision: Decision,
    val enabled: Boolean,
    val cost: Int,
    val tool: ToolId? = null,
    val blockedByFatigue: Boolean = false
)
