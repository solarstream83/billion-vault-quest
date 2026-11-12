package com.Pacvazo7.Rilcer.domain.model

enum class ArtifactSetId { MINERAL_RELICS, BRASS_MECHANISMS, CORE_FRAGMENTS }

data class Artifact(
    val id: Int,
    val set: ArtifactSetId
)

data class ArtifactSet(
    val id: ArtifactSetId,
    val artifacts: List<Artifact>
)
