package com.Pacvazo7.Rilcer.presentation.gameover

import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.domain.model.RunResult

data class GameOverUiState(
    val result: RunResult? = null,
    val outcome: RunOutcome = RunOutcome.TEAM_EXTRACTED,
    val crystalBalance: Int = 0,
    val reducedMotion: Boolean = false,
    val replayReady: Boolean = false
)
