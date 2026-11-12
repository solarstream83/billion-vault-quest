package com.Pacvazo7.Rilcer.domain.repository

import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.RunResult
import com.Pacvazo7.Rilcer.domain.model.Specialist
import kotlinx.coroutines.flow.StateFlow

interface RunSessionRepository {
    val expedition: StateFlow<ExpeditionState?>

    val lastResult: StateFlow<RunResult?>

    val lastTeam: StateFlow<List<Specialist>>

    fun begin(state: ExpeditionState)

    fun mutate(transform: (ExpeditionState) -> ExpeditionState)

    fun rememberTeam(team: List<Specialist>)

    fun publishResult(result: RunResult)

    fun clearRun()
}
