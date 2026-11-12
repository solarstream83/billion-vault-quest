package com.Pacvazo7.Rilcer.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val GRID_STANDARD = 5
    const val GRID_DEEP = 6

    const val BOARD_MAX_WIDTH_DP = 380
    const val BOARD_SIDE_MARGIN_DP = 32
    const val BOARD_PAD_DP = 6
    const val BOARD_BORDER_DP = 2
    const val TILE_GAP_DP = 3

    const val TEAM_SIZE = 3
    const val DEFAULT_SUPPLIES = 12
    const val LEAN_SUPPLIES = 9
    const val RICH_SUPPLIES = 15

    const val MOVE_COST = 1
    const val FALL_BACK_COST = 1
    const val PUSH_ON_TRAP_COST = 4
    const val PORTER_SUPPLY_BONUS = 3
    const val SET_SUPPLY_BONUS = 2

    const val CHARGES_BASE = 2
    const val CHARGES_MASTER_BONUS = 1

    const val INSTABILITY_PER_TURN = 1
    const val INSTABILITY_PUSH_ON = 3
    const val COLLAPSE_FIRST = 10
    const val COLLAPSE_SECOND = 16

    const val CACHE_CRYSTALS_BASE = 12
    const val CACHE_CRYSTALS_SPREAD = 9
    const val CORE_BONUS_BASE = 100
    const val CORE_BONUS_PER_ROOM = 15
    const val OBJECTIVE_REWARD = 60

    const val TOOL_LEVEL_TWO_COST = 40
    const val TOOL_LEVEL_THREE_COST = 110
    const val TOOL_MAX_LEVEL = 3

    const val ARTIFACT_SET_SIZE = 4
    const val ARTIFACT_TOTAL = 12
    const val CACHE_ARTIFACT_EVERY = 3

    const val LOW_SUPPLY_WARNING = 3
    const val GENERATION_ATTEMPTS = 20
}
