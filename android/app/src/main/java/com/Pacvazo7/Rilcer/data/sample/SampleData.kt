package com.Pacvazo7.Rilcer.data.sample

import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.domain.model.Artifact
import com.Pacvazo7.Rilcer.domain.model.ArtifactSet
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.Specialist
import com.Pacvazo7.Rilcer.domain.model.ToolId

object SampleData {

    val specialists: List<Specialist> = listOf(
        Specialist.SURVEYOR,
        Specialist.ENGINEER,
        Specialist.LOCKSMITH,
        Specialist.PORTER,
        Specialist.GEOLOGIST
    )

    val tools: List<ToolId> = listOf(
        ToolId.SCANNER,
        ToolId.BRACE,
        ToolId.LOCKPICK,
        ToolId.PACK,
        ToolId.CORE_SENSE
    )

    val artifactSets: List<ArtifactSet> = ArtifactSetId.entries.mapIndexed { setIndex, setId ->
        val first = setIndex * GameConfig.ARTIFACT_SET_SIZE
        ArtifactSet(
            id = setId,
            artifacts = (first until first + GameConfig.ARTIFACT_SET_SIZE).map { id ->
                Artifact(id = id, set = setId)
            }
        )
    }

    val allArtifacts: List<Artifact> = artifactSets.flatMap { it.artifacts }
}
