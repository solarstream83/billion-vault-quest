package com.Pacvazo7.Rilcer.core.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.Pacvazo7.Rilcer.presentation.app.AppViewModel
import com.Pacvazo7.Rilcer.presentation.collection.ArtifactCollectionViewModel
import com.Pacvazo7.Rilcer.presentation.exploration.ExplorationTurnViewModel
import com.Pacvazo7.Rilcer.presentation.gameover.GameOverViewModel
import com.Pacvazo7.Rilcer.presentation.menu.MenuViewModel
import com.Pacvazo7.Rilcer.presentation.settings.SettingsViewModel
import com.Pacvazo7.Rilcer.presentation.splash.SplashViewModel
import com.Pacvazo7.Rilcer.presentation.teamsetup.TeamSetupViewModel
import com.Pacvazo7.Rilcer.presentation.vaultmap.VaultMapViewModel
import com.Pacvazo7.Rilcer.presentation.workshop.ToolWorkshopViewModel

object AppViewModelFactory {

    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { AppViewModel() }
        initializer { SplashViewModel() }
        initializer {
            MenuViewModel(
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                evaluateObjective = ServiceLocator.evaluateObjective
            )
        }
        initializer {
            TeamSetupViewModel(
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                sessionRepository = ServiceLocator.sessionRepository,
                startExpedition = ServiceLocator.startExpedition
            )
        }
        initializer {
            VaultMapViewModel(
                sessionRepository = ServiceLocator.sessionRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                progressRepository = ServiceLocator.progressRepository,
                advanceInstability = ServiceLocator.advanceInstability,
                finishExpedition = ServiceLocator.finishExpedition,
                startExpedition = ServiceLocator.startExpedition
            )
        }
        initializer {
            ExplorationTurnViewModel(
                sessionRepository = ServiceLocator.sessionRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                resolveEncounter = ServiceLocator.resolveEncounter,
                finishExpedition = ServiceLocator.finishExpedition
            )
        }
        initializer {
            ToolWorkshopViewModel(
                progressRepository = ServiceLocator.progressRepository,
                upgradeTool = ServiceLocator.upgradeTool
            )
        }
        initializer {
            ArtifactCollectionViewModel(
                progressRepository = ServiceLocator.progressRepository
            )
        }
        initializer {
            GameOverViewModel(
                sessionRepository = ServiceLocator.sessionRepository,
                progressRepository = ServiceLocator.progressRepository,
                settingsRepository = ServiceLocator.settingsRepository,
                startExpedition = ServiceLocator.startExpedition
            )
        }
        initializer {
            SettingsViewModel(
                settingsRepository = ServiceLocator.settingsRepository,
                progressRepository = ServiceLocator.progressRepository
            )
        }
    }
}
