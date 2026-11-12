package com.Pacvazo7.Rilcer.presentation.app

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pacvazo7.Rilcer.core.di.AppViewModelFactory
import com.Pacvazo7.Rilcer.core.navigation.Screen
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.presentation.collection.ArtifactCollectionScreen
import com.Pacvazo7.Rilcer.presentation.exploration.ExplorationTurnScreen
import com.Pacvazo7.Rilcer.presentation.gameover.GameOverScreen
import com.Pacvazo7.Rilcer.presentation.menu.MenuScreen
import com.Pacvazo7.Rilcer.presentation.settings.SettingsScreen
import com.Pacvazo7.Rilcer.presentation.splash.SplashScreen
import com.Pacvazo7.Rilcer.presentation.teamsetup.TeamSetupScreen
import com.Pacvazo7.Rilcer.presentation.vaultmap.VaultMapScreen
import com.Pacvazo7.Rilcer.presentation.workshop.ToolWorkshopScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory.factory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val context = LocalContext.current

    BackHandler(enabled = screen != Screen.Splash) {
        if (!appViewModel.back()) {
            (context as? Activity)?.finish()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = { fadeIn(tween(320)) togetherWith fadeOut(tween(220)) },
            label = "screenHost"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashScreen(
                    onFinished = { appViewModel.openMenu() }
                )

                Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.navigate(Screen.TeamSetup) },
                    onWorkshop = { appViewModel.navigate(Screen.ToolWorkshop) },
                    onCollection = { appViewModel.navigate(Screen.ArtifactCollection) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                Screen.TeamSetup -> TeamSetupScreen(
                    onBack = { appViewModel.popToMenu() },
                    onEnterVault = { appViewModel.navigate(Screen.VaultMap) }
                )

                Screen.VaultMap -> VaultMapScreen(
                    onOpenRoom = { index ->
                        appViewModel.navigate(Screen.ExplorationTurn(index))
                    },
                    onSummary = { outcome ->
                        appViewModel.finishRunWith(Screen.GameOver(outcome))
                    },
                    onMenu = { appViewModel.popToMenu() }
                )

                is Screen.ExplorationTurn -> ExplorationTurnScreen(
                    roomIndex = target.roomIndex,
                    onBackToMap = {
                        if (!appViewModel.back()) appViewModel.replace(Screen.VaultMap)
                    },
                    onSummary = { outcome ->
                        appViewModel.finishRunWith(Screen.GameOver(outcome))
                    }
                )

                Screen.ToolWorkshop -> ToolWorkshopScreen(
                    onBack = { appViewModel.back() }
                )

                Screen.ArtifactCollection -> ArtifactCollectionScreen(
                    onBack = { appViewModel.back() }
                )

                is Screen.GameOver -> GameOverScreen(
                    outcome = target.outcome,
                    onPlayAgain = { appViewModel.replace(Screen.VaultMap) },
                    onWorkshop = { appViewModel.navigate(Screen.ToolWorkshop) },
                    onMenu = { appViewModel.popToMenu() },
                    onPickTeam = { appViewModel.replace(Screen.TeamSetup) }
                )

                Screen.Settings -> SettingsScreen(
                    onBack = { appViewModel.back() }
                )
            }
        }
    }
}
