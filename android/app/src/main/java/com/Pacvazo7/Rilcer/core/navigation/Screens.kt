package com.Pacvazo7.Rilcer.core.navigation

import com.Pacvazo7.Rilcer.domain.model.RunOutcome

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object TeamSetup : Screen

    data object VaultMap : Screen

    data class ExplorationTurn(val roomIndex: Int) : Screen

    data object ToolWorkshop : Screen

    data object ArtifactCollection : Screen

    data class GameOver(val outcome: RunOutcome) : Screen

    data object Settings : Screen
}
