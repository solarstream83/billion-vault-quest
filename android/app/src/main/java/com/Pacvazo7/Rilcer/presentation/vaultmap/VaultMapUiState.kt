package com.Pacvazo7.Rilcer.presentation.vaultmap

import com.Pacvazo7.Rilcer.domain.model.ExpeditionState

data class VaultMapUiState(
    val expedition: ExpeditionState? = null,
    val pauseVisible: Boolean = false,
    val abandonVisible: Boolean = false,
    val reducedMotion: Boolean = false
) {
    val canGo: Boolean get() = expedition?.selectedIndex != null

    val canFallBack: Boolean
        get() {
            val run = expedition ?: return false
            return run.previousIndex != run.teamIndex &&
                run.isAdjacent(run.previousIndex, run.teamIndex) &&
                !run.rooms[run.previousIndex].collapsed
        }
}
