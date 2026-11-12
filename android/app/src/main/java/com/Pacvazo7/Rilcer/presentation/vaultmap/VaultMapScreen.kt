package com.Pacvazo7.Rilcer.presentation.vaultmap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.core.di.AppViewModelFactory
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldDeep
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.Malachite
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceWarm
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.IconTarget
import com.Pacvazo7.Rilcer.core.ui.PrimaryButton
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.StatCardData
import com.Pacvazo7.Rilcer.core.ui.StatRow
import com.Pacvazo7.Rilcer.core.ui.SupplyMeter
import com.Pacvazo7.Rilcer.core.ui.VaultBackground
import com.Pacvazo7.Rilcer.core.ui.VaultTile
import com.Pacvazo7.Rilcer.domain.model.ExpeditionState
import com.Pacvazo7.Rilcer.domain.model.RunOutcome
import com.Pacvazo7.Rilcer.presentation.dialog.ConfirmDialog
import com.Pacvazo7.Rilcer.presentation.dialog.PauseDialog
import kotlin.math.floor

@Composable
fun VaultMapScreen(
    onOpenRoom: (Int) -> Unit,
    onSummary: (RunOutcome) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: VaultMapViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is VaultMapEvent.OpenRoom -> onOpenRoom(event.index)
                is VaultMapEvent.ShowSummary -> onSummary(event.outcome)
                VaultMapEvent.LeaveToMenu -> onMenu()
            }
        }
    }

    DisposableEffect(viewModel) {
        onDispose { viewModel.pause() }
    }

    VaultMapContent(
        state = state,
        onSelect = viewModel::select,
        onGo = viewModel::go,
        onFallBack = viewModel::fallBack,
        onExtract = viewModel::extract,
        onPause = viewModel::showPause,
        onResume = viewModel::hidePause,
        onRestart = viewModel::restart,
        onAskAbandon = viewModel::askAbandon,
        onDismissAbandon = viewModel::dismissAbandon,
        onAbandon = viewModel::abandon,
        onMenu = onMenu
    )
}

@Composable
private fun VaultMapContent(
    state: VaultMapUiState,
    onSelect: (Int) -> Unit,
    onGo: () -> Unit,
    onFallBack: () -> Unit,
    onExtract: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onAskAbandon: () -> Unit,
    onDismissAbandon: () -> Unit,
    onAbandon: () -> Unit,
    onMenu: () -> Unit
) {
    val overlay = Brush.verticalGradient(
        0f to BackgroundDeep.copy(alpha = 0.92f),
        0.5f to BackgroundDeep.copy(alpha = 0.88f),
        1f to BackgroundDeep.copy(alpha = 0.96f)
    )
    val run = state.expedition

    VaultBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(Color.Black.copy(alpha = 0.35f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconTarget(
                    imageRes = R.drawable.icon_pause,
                    descriptionRes = R.string.cd_pause,
                    onClick = onPause
                )
                Text(
                    text = if (run == null) {
                        stringResource(R.string.title_vault_map)
                    } else {
                        stringResource(R.string.header_turn_depth, run.turn, run.roomsCleared)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                IconTarget(
                    imageRes = R.drawable.icon_home,
                    descriptionRes = R.string.cd_back_home,
                    onClick = onAskAbandon
                )
            }

            if (run == null) {
                EmptyRun(modifier = Modifier.weight(1f), onMenu = onMenu)
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        VaultBoard(
                            run = run,
                            reducedMotion = state.reducedMotion,
                            onSelect = onSelect
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.map_hint),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                val liveCards = listOf(
                    StatCardData(
                        labelRes = R.string.stat_supplies,
                        amount = run.supplies,
                        display = run.supplies.toString(),
                        accent = if (run.supplies <= GameConfig.LOW_SUPPLY_WARNING) {
                            Ember
                        } else {
                            Gold
                        },
                        iconRes = R.drawable.ic_supply,
                        announce = true
                    ),
                    StatCardData(
                        labelRes = R.string.stat_instability,
                        amount = run.instability,
                        display = run.instability.toString(),
                        accent = Ember,
                        imageRes = R.drawable.sprite_4,
                        announce = true
                    ),
                    StatCardData(
                        labelRes = R.string.stat_crystals,
                        amount = run.crystals,
                        display = run.crystals.toString(),
                        accent = Malachite,
                        imageRes = R.drawable.sprite_3
                    )
                )
                val turnCard = StatCardData(
                    labelRes = R.string.stat_turns,
                    amount = run.turn,
                    display = run.turn.toString(),
                    accent = GoldLight
                )
                StatRow(
                    cards = if (liveCards.count { it.amount != 0 } >= 2) {
                        liveCards
                    } else {
                        liveCards + turnCard
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                ControlBlock(
                    state = state,
                    run = run,
                    onGo = onGo,
                    onFallBack = onFallBack,
                    onExtract = onExtract
                )
            }
        }
    }

    if (state.pauseVisible) {
        PauseDialog(
            onResume = onResume,
            onRestart = onRestart,
            onMenu = onAskAbandon
        )
    }
    if (state.abandonVisible) {
        ConfirmDialog(
            titleRes = R.string.dialog_abandon_title,
            messageRes = R.string.dialog_abandon_message,
            confirmRes = R.string.action_menu,
            dismissRes = R.string.action_cancel,
            onConfirm = onAbandon,
            onDismiss = onDismissAbandon
        )
    }
}

@Composable
private fun ControlBlock(
    state: VaultMapUiState,
    run: ExpeditionState,
    onGo: () -> Unit,
    onFallBack: () -> Unit,
    onExtract: () -> Unit
) {
    val shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(SurfaceBase.copy(alpha = 0.96f), BackgroundDeep.copy(alpha = 0.99f))
                ),
                shape
            )
            .border(1.dp, Gold.copy(alpha = 0.4f), shape)
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        SupplyMeter(current = run.supplies, max = run.startingSupplies)
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            textRes = R.string.action_go,
            onClick = onGo,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canGo
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                textRes = R.string.action_fall_back,
                onClick = onFallBack,
                modifier = Modifier.weight(1f),
                enabled = state.canFallBack
            )
            SecondaryButton(
                textRes = R.string.action_extract,
                onClick = onExtract,
                modifier = Modifier.weight(1f),
                accent = Malachite
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
private fun VaultBoard(
    run: ExpeditionState,
    reducedMotion: Boolean,
    onSelect: (Int) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val available = maxWidth - GameConfig.BOARD_SIDE_MARGIN_DP.dp
        val ceiling = GameConfig.BOARD_MAX_WIDTH_DP.dp
        val boardMax: Dp = if (available < ceiling) available else ceiling
        val frame = (GameConfig.BOARD_PAD_DP + GameConfig.BOARD_BORDER_DP).dp
        val inner = boardMax.value - frame.value * 2f
        val tile = floor(inner / run.cols).dp
        val boardWidth = tile * run.cols + frame * 2

        val shape = RoundedCornerShape(14.dp)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(boardWidth)
                .shadow(16.dp, shape, spotColor = Color.Black)
                .background(SurfaceWarm, shape)
                .border(
                    width = GameConfig.BOARD_BORDER_DP.dp,
                    brush = Brush.linearGradient(listOf(Gold, GoldDeep)),
                    shape = shape
                )
                .padding(frame)
        ) {
            for (row in 0 until run.rows) {
                Row {
                    for (col in 0 until run.cols) {
                        val index = row * run.cols + col
                        VaultTile(
                            room = run.rooms[index],
                            size = tile,
                            selected = run.selectedIndex == index,
                            hasTeam = run.teamIndex == index,
                            reducedMotion = reducedMotion,
                            onClick = { onSelect(index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyRun(onMenu: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.map_no_run),
            style = MaterialTheme.typography.bodyLarge,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryButton(
            textRes = R.string.action_menu,
            onClick = onMenu,
            modifier = Modifier.fillMaxWidth(),
            accent = GoldLight
        )
    }
}
