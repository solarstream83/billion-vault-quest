package com.Pacvazo7.Rilcer.presentation.teamsetup

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.Specialist

data class TeamSetupUiState(
    val slots: List<Specialist?> = List(GameConfig.TEAM_SIZE) { null },
    val roster: List<Specialist> = emptyList(),
    val openSlot: Int? = null,
    val suppliesPreview: Int = GameConfig.DEFAULT_SUPPLIES,
    val setBonuses: List<ArtifactSetId> = emptyList(),
    val reducedMotion: Boolean = false
) {
    val assigned: List<Specialist> get() = slots.filterNotNull()

    val ready: Boolean get() = slots.none { it == null }
}
