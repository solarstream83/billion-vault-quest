package com.Pacvazo7.Rilcer.presentation.collection

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.Artifact
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId

data class ArtifactSetState(
    val id: ArtifactSetId,
    val artifacts: List<Artifact>,
    val owned: Set<Int>,
    val complete: Boolean
)

data class ArtifactCollectionUiState(
    val sets: List<ArtifactSetState> = emptyList(),
    val ownedCount: Int = 0,
    val total: Int = GameConfig.ARTIFACT_TOTAL
) {
    val empty: Boolean get() = ownedCount == 0
}
