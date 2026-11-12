package com.Pacvazo7.Rilcer.data.repository

import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.RunResult
import com.Pacvazo7.Rilcer.domain.model.Specialist
import com.Pacvazo7.Rilcer.domain.repository.RunSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RunSessionRepositoryImpl : RunSessionRepository {

    private val runState = MutableStateFlow<ExpeditionState?>(null)
    private val resultState = MutableStateFlow<RunResult?>(null)
    private val teamState = MutableStateFlow<List<Specialist>>(emptyList())

    override val expedition: StateFlow<ExpeditionState?> = runState.asStateFlow()

    override val lastResult: StateFlow<RunResult?> = resultState.asStateFlow()

    override val lastTeam: StateFlow<List<Specialist>> = teamState.asStateFlow()

    override fun begin(state: ExpeditionState) {
        runState.value = state
        teamState.value = state.team
    }

    override fun mutate(transform: (ExpeditionState) -> ExpeditionState) {
        runState.update { current -> current?.let(transform) }
    }

    override fun rememberTeam(team: List<Specialist>) {
        teamState.value = team
    }

    override fun publishResult(result: RunResult) {
        resultState.value = result
    }

    override fun clearRun() {
        runState.value = null
    }
}
