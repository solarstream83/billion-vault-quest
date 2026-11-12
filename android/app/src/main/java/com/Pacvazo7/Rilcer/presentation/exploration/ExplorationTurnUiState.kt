package com.Pacvazo7.Rilcer.presentation.exploration

import com.Pacvazo7.Rilcer.domain.model.DecisionOption
import com.Pacvazo7.Rilcer.domain.model.EncounterOutcome
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.model.TrapKind

data class ExplorationTurnUiState(
    val roomIndex: Int = -1,
    val kind: RoomKind = RoomKind.EMPTY,
    val trap: TrapKind? = null,
    val options: List<DecisionOption> = emptyList(),
    val outcome: EncounterOutcome? = null,
    val supplies: Int = 0,
    val crystals: Int = 0,
    val suppliesDelta: Int = 0,
    val crystalsDelta: Int = 0,
    val fatiguedTool: Boolean = false,
    val runOutcome: RunOutcome? = null,
    val reducedMotion: Boolean = false
) {
    val resolved: Boolean get() = outcome != null

    val runEnded: Boolean get() = runOutcome != null
}
