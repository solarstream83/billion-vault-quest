package com.Pacvazo7.Rilcer.core.di

import android.content.Context
import com.Pacvazo7.Rilcer.data.local.PreferencesStorage
import com.Pacvazo7.Rilcer.data.repository.ProgressRepositoryImpl
import com.Pacvazo7.Rilcer.data.repository.RunSessionRepositoryImpl
import com.Pacvazo7.Rilcer.data.repository.SettingsRepositoryImpl
import com.Pacvazo7.Rilcer.domain.repository.ProgressRepository
import com.Pacvazo7.Rilcer.domain.repository.RunSessionRepository
import com.Pacvazo7.Rilcer.domain.repository.SettingsRepository
import com.Pacvazo7.Rilcer.domain.usecase.AdvanceInstabilityUseCase
import com.Pacvazo7.Rilcer.domain.usecase.ApplyClueUseCase
import com.Pacvazo7.Rilcer.domain.usecase.EvaluateObjectiveUseCase
import com.Pacvazo7.Rilcer.domain.usecase.FinishExpeditionUseCase
import com.Pacvazo7.Rilcer.domain.usecase.GenerateVaultUseCase
import com.Pacvazo7.Rilcer.domain.usecase.ResolveEncounterUseCase
import com.Pacvazo7.Rilcer.domain.usecase.StartExpeditionUseCase
import com.Pacvazo7.Rilcer.domain.usecase.UpgradeToolUseCase

object ServiceLocator {

    private var container: Container? = null

    fun init(context: Context) {
        if (container != null) return
        val storage = PreferencesStorage(context)
        val progress = ProgressRepositoryImpl(storage)
        val settings = SettingsRepositoryImpl(storage)
        val session = RunSessionRepositoryImpl()
        val applyClue = ApplyClueUseCase()
        val evaluateObjective = EvaluateObjectiveUseCase()
        container = Container(
            progressRepository = progress,
            settingsRepository = settings,
            sessionRepository = session,
            applyClue = applyClue,
            evaluateObjective = evaluateObjective,
            startExpedition = StartExpeditionUseCase(GenerateVaultUseCase(), applyClue),
            resolveEncounter = ResolveEncounterUseCase(applyClue),
            advanceInstability = AdvanceInstabilityUseCase(),
            upgradeTool = UpgradeToolUseCase(progress),
            finishExpedition = FinishExpeditionUseCase(progress, evaluateObjective)
        )
    }

    private fun require(): Container = requireNotNull(container)

    val progressRepository: ProgressRepository get() = require().progressRepository
    val settingsRepository: SettingsRepository get() = require().settingsRepository
    val sessionRepository: RunSessionRepository get() = require().sessionRepository
    val applyClue: ApplyClueUseCase get() = require().applyClue
    val evaluateObjective: EvaluateObjectiveUseCase get() = require().evaluateObjective
    val startExpedition: StartExpeditionUseCase get() = require().startExpedition
    val resolveEncounter: ResolveEncounterUseCase get() = require().resolveEncounter
    val advanceInstability: AdvanceInstabilityUseCase get() = require().advanceInstability
    val upgradeTool: UpgradeToolUseCase get() = require().upgradeTool
    val finishExpedition: FinishExpeditionUseCase get() = require().finishExpedition

    private class Container(
        val progressRepository: ProgressRepository,
        val settingsRepository: SettingsRepository,
        val sessionRepository: RunSessionRepository,
        val applyClue: ApplyClueUseCase,
        val evaluateObjective: EvaluateObjectiveUseCase,
        val startExpedition: StartExpeditionUseCase,
        val resolveEncounter: ResolveEncounterUseCase,
        val advanceInstability: AdvanceInstabilityUseCase,
        val upgradeTool: UpgradeToolUseCase,
        val finishExpedition: FinishExpeditionUseCase
    )
}
