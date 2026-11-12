package com.Pacvazo7.Rilcer.core.ui

import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.domain.model.ArtifactSetId
import com.Pacvazo7.Rilcer.domain.model.Heading
import com.Pacvazo7.Rilcer.domain.model.ObjectiveId
import com.Pacvazo7.Rilcer.domain.model.OutcomeKind
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.Specialist
import com.Pacvazo7.Rilcer.domain.model.ToolId
import com.Pacvazo7.Rilcer.domain.model.TrapKind

object Labels {

    fun specialistName(specialist: Specialist): Int = when (specialist) {
        Specialist.SURVEYOR -> R.string.specialist_surveyor
        Specialist.ENGINEER -> R.string.specialist_engineer
        Specialist.LOCKSMITH -> R.string.specialist_locksmith
        Specialist.PORTER -> R.string.specialist_porter
        Specialist.GEOLOGIST -> R.string.specialist_geologist
    }

    fun specialistEffect(specialist: Specialist): Int = when (specialist) {
        Specialist.SURVEYOR -> R.string.specialist_surveyor_effect
        Specialist.ENGINEER -> R.string.specialist_engineer_effect
        Specialist.LOCKSMITH -> R.string.specialist_locksmith_effect
        Specialist.PORTER -> R.string.specialist_porter_effect
        Specialist.GEOLOGIST -> R.string.specialist_geologist_effect
    }

    fun toolName(tool: ToolId): Int = when (tool) {
        ToolId.SCANNER -> R.string.tool_scanner
        ToolId.BRACE -> R.string.tool_brace
        ToolId.LOCKPICK -> R.string.tool_lockpick
        ToolId.PACK -> R.string.tool_pack
        ToolId.CORE_SENSE -> R.string.tool_core_sense
    }

    fun toolEffect(tool: ToolId): Int = when (tool) {
        ToolId.SCANNER -> R.string.tool_scanner_effect
        ToolId.BRACE -> R.string.tool_brace_effect
        ToolId.LOCKPICK -> R.string.tool_lockpick_effect
        ToolId.PACK -> R.string.tool_pack_effect
        ToolId.CORE_SENSE -> R.string.tool_core_sense_effect
    }

    fun toolIcon(tool: ToolId): Int = when (tool) {
        ToolId.SCANNER -> R.drawable.ic_scanner
        ToolId.BRACE -> R.drawable.ic_brace
        ToolId.LOCKPICK -> R.drawable.ic_lockpick
        ToolId.PACK -> R.drawable.ic_pack
        ToolId.CORE_SENSE -> R.drawable.ic_core_sense
    }

    fun artifactSetName(set: ArtifactSetId): Int = when (set) {
        ArtifactSetId.MINERAL_RELICS -> R.string.set_mineral_relics
        ArtifactSetId.BRASS_MECHANISMS -> R.string.set_brass_mechanisms
        ArtifactSetId.CORE_FRAGMENTS -> R.string.set_core_fragments
    }

    fun artifactSetBonus(set: ArtifactSetId): Int = when (set) {
        ArtifactSetId.MINERAL_RELICS -> R.string.set_bonus_mineral
        ArtifactSetId.BRASS_MECHANISMS -> R.string.set_bonus_brass
        ArtifactSetId.CORE_FRAGMENTS -> R.string.set_bonus_core
    }

    fun artifactName(id: Int): Int = ARTIFACT_NAMES.getOrElse(id) { R.string.artifact_unknown }

    fun objectiveName(objective: ObjectiveId): Int = when (objective) {
        ObjectiveId.CLEAR_EIGHT_ROOMS -> R.string.objective_clear_rooms
        ObjectiveId.RECOVER_TWO_ARTIFACTS -> R.string.objective_artifacts
        ObjectiveId.UPGRADE_ANY_TOOL -> R.string.objective_upgrade
        ObjectiveId.COMPLETE_ONE_SET -> R.string.objective_set
        ObjectiveId.RECOVER_THE_CORE -> R.string.objective_core
        ObjectiveId.RECOVER_CORE_DEEP -> R.string.objective_core_deep
    }

    fun roomName(kind: RoomKind, trap: TrapKind?): Int = when (kind) {
        RoomKind.CLUE -> R.string.room_survey_mark
        RoomKind.CACHE -> R.string.room_crystal_pocket
        RoomKind.CORE -> R.string.room_the_core
        RoomKind.EMPTY -> R.string.room_empty_gallery
        RoomKind.TRAP -> if (trap == TrapKind.COLLAPSE) {
            R.string.room_collapsed_shaft
        } else {
            R.string.room_brass_seal
        }
    }

    fun roomDescription(kind: RoomKind, trap: TrapKind?): Int = when (kind) {
        RoomKind.CLUE -> R.string.room_survey_mark_desc
        RoomKind.CACHE -> R.string.room_crystal_pocket_desc
        RoomKind.CORE -> R.string.room_the_core_desc
        RoomKind.EMPTY -> R.string.room_empty_gallery_desc
        RoomKind.TRAP -> if (trap == TrapKind.COLLAPSE) {
            R.string.room_collapsed_shaft_desc
        } else {
            R.string.room_brass_seal_desc
        }
    }

    fun heading(heading: Heading): Int = when (heading) {
        Heading.NORTH -> R.string.heading_north
        Heading.NORTH_EAST -> R.string.heading_north_east
        Heading.EAST -> R.string.heading_east
        Heading.SOUTH_EAST -> R.string.heading_south_east
        Heading.SOUTH -> R.string.heading_south
        Heading.SOUTH_WEST -> R.string.heading_south_west
        Heading.WEST -> R.string.heading_west
        Heading.NORTH_WEST -> R.string.heading_north_west
    }

    fun outcomeTitle(kind: OutcomeKind): Int = when (kind) {
        OutcomeKind.BRACE_HOLDS -> R.string.outcome_brace_holds
        OutcomeKind.SEAL_PICKED -> R.string.outcome_seal_picked
        OutcomeKind.SUPPLIES_SPILLED -> R.string.outcome_supplies_spilled
        OutcomeKind.CRYSTALS_POCKETED -> R.string.outcome_crystals_pocketed
        OutcomeKind.CRYSTALS_SCRAPED -> R.string.outcome_crystals_scraped
        OutcomeKind.SURVEY_PRECISE -> R.string.outcome_survey_precise
        OutcomeKind.SURVEY_VAGUE -> R.string.outcome_survey_vague
        OutcomeKind.SCANNER_PEEK -> R.string.outcome_scanner_peek
        OutcomeKind.EMPTY_GALLERY -> R.string.outcome_empty_gallery
        OutcomeKind.CORE_RECOVERED -> R.string.outcome_core_recovered
        OutcomeKind.STEPPED_BACK -> R.string.outcome_stepped_back
    }

    private val ARTIFACT_NAMES = listOf(
        R.string.artifact_0,
        R.string.artifact_1,
        R.string.artifact_2,
        R.string.artifact_3,
        R.string.artifact_4,
        R.string.artifact_5,
        R.string.artifact_6,
        R.string.artifact_7,
        R.string.artifact_8,
        R.string.artifact_9,
        R.string.artifact_10,
        R.string.artifact_11
    )
}
