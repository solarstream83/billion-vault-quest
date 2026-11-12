package com.Pacvazo7.Rilcer.domain.model

enum class ObjectiveId(val target: Int) {
    CLEAR_EIGHT_ROOMS(8),
    RECOVER_TWO_ARTIFACTS(2),
    UPGRADE_ANY_TOOL(1),
    COMPLETE_ONE_SET(1),
    RECOVER_THE_CORE(1),
    RECOVER_CORE_DEEP(1)
}

data class ObjectiveProgress(
    val id: ObjectiveId,
    val current: Int,
    val target: Int,
    val completed: Boolean
)
