package com.Pacvazo7.Rilcer.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.di.AppViewModelFactory
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.ExtractTop
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.LoseTop
import com.Pacvazo7.Rilcer.core.theme.Malachite
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.theme.WinTop
import com.Pacvazo7.Rilcer.core.ui.GlassCard
import com.Pacvazo7.Rilcer.core.ui.Labels
import com.Pacvazo7.Rilcer.core.ui.PrimaryButton
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.StatCardData
import com.Pacvazo7.Rilcer.core.ui.StatRow
import com.Pacvazo7.Rilcer.core.ui.VaultBackground
import com.Pacvazo7.Rilcer.domain.model.RunOutcome

@Composable
fun GameOverScreen(
    outcome: RunOutcome,
    onPlayAgain: () -> Unit,
    onWorkshop: () -> Unit,
    onMenu: () -> Unit,
    onPickTeam: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(outcome) {
        viewModel.bind(outcome)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                GameOverEvent.EnterVault -> onPlayAgain()
                GameOverEvent.PickTeam -> onPickTeam()
            }
        }
    }

    GameOverContent(
        state = state,
        onPlayAgain = viewModel::playAgain,
        onWorkshop = onWorkshop,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onWorkshop: () -> Unit,
    onMenu: () -> Unit
) {
    val outcome = state.outcome
    val accent = when (outcome) {
        RunOutcome.CORE_RECOVERED -> GoldLight
        RunOutcome.TEAM_EXTRACTED -> Malachite
        RunOutcome.SUPPLIES_GONE -> Ember
    }
    val top = when (outcome) {
        RunOutcome.CORE_RECOVERED -> WinTop
        RunOutcome.TEAM_EXTRACTED -> ExtractTop
        RunOutcome.SUPPLIES_GONE -> LoseTop
    }
    val overlay = Brush.verticalGradient(
        0f to top.copy(alpha = 0.88f),
        0.6f to BackgroundDeep.copy(alpha = 0.94f),
        1f to BackgroundDeep.copy(alpha = 0.97f)
    )
    val heroScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 300f),
        label = "heroEntry"
    )
    val result = state.result

    VaultBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = stringResource(R.string.title_summary),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    listOf(accent.copy(alpha = 0.32f), Color.Transparent)
                                ),
                                RoundedCornerShape(70.dp)
                            )
                    )
                    Image(
                        painter = painterResource(
                            if (outcome == RunOutcome.CORE_RECOVERED) {
                                R.drawable.sprite_5
                            } else {
                                R.drawable.sprite_1
                            }
                        ),
                        contentDescription = stringResource(R.string.cd_result_hero),
                        modifier = Modifier
                            .size(118.dp)
                            .scale(heroScale),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(
                        when (outcome) {
                            RunOutcome.CORE_RECOVERED -> R.string.result_core_recovered
                            RunOutcome.TEAM_EXTRACTED -> R.string.result_team_extracted
                            RunOutcome.SUPPLIES_GONE -> R.string.result_supplies_gone
                        }
                    ),
                    style = MaterialTheme.typography.displaySmall,
                    color = accent,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (result != null) {
                    GlassCard(contentPadding = 14.dp) {
                        StatRow(
                            cards = listOf(
                                StatCardData(
                                    labelRes = R.string.stat_rooms_cleared,
                                    amount = result.roomsCleared,
                                    display = result.roomsCleared.toString(),
                                    accent = Gold
                                ),
                                StatCardData(
                                    labelRes = R.string.stat_crystals,
                                    amount = result.crystalsBanked,
                                    display = result.crystalsBanked.toString(),
                                    accent = Malachite,
                                    imageRes = R.drawable.sprite_3
                                )
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        StatRow(
                            cards = listOf(
                                StatCardData(
                                    labelRes = R.string.stat_artifacts,
                                    amount = result.artifacts.size,
                                    display = result.artifacts.size.toString(),
                                    accent = GoldLight,
                                    imageRes = R.drawable.sprite_5
                                ),
                                StatCardData(
                                    labelRes = R.string.stat_turns,
                                    amount = result.turns,
                                    display = result.turns.toString(),
                                    accent = TextSecondary
                                )
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    if (result.newRecord) {
                        Text(
                            text = stringResource(R.string.result_new_record),
                            style = MaterialTheme.typography.titleMedium,
                            color = GoldLight,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    result.completedSets.forEach { set ->
                        Text(
                            text = stringResource(
                                R.string.result_set_complete,
                                stringResource(Labels.artifactSetName(set))
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = Malachite,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (result.objectiveJustCompleted) {
                        Text(
                            text = stringResource(R.string.result_objective_done),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    val objective = result.objective
                    if (objective != null && !result.objectiveJustCompleted) {
                        Text(
                            text = stringResource(
                                R.string.result_objective,
                                stringResource(Labels.objectiveName(objective.id)),
                                objective.current,
                                objective.target
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(StoneLine)
                        ) {
                            val ratio = if (objective.target <= 0) {
                                0f
                            } else {
                                objective.current.toFloat() / objective.target.toFloat()
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(ratio.coerceIn(0f, 1f))
                                    .height(2.dp)
                                    .background(Gold)
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.result_no_data),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.result_balance, state.crystalBalance),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            PrimaryButton(
                textRes = R.string.action_play_again,
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    textRes = R.string.action_workshop,
                    onClick = onWorkshop,
                    modifier = Modifier.weight(1f),
                    leadingIcon = R.drawable.ic_tool
                )
                SecondaryButton(
                    textRes = R.string.action_menu,
                    onClick = onMenu,
                    modifier = Modifier.weight(1f),
                    leadingIcon = R.drawable.icon_home
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
