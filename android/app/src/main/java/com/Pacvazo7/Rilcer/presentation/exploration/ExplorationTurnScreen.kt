package com.Pacvazo7.Rilcer.presentation.exploration

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.Malachite
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.GlassCard
import com.Pacvazo7.Rilcer.core.ui.Labels
import com.Pacvazo7.Rilcer.core.ui.PrimaryButton
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.VaultBackground
import com.Pacvazo7.Rilcer.domain.model.Decision
import com.Pacvazo7.Rilcer.domain.model.EncounterOutcome
import com.Pacvazo7.Rilcer.domain.model.Heading
import com.Pacvazo7.Rilcer.domain.model.OutcomeKind
import com.Pacvazo7.Rilcer.domain.model.RoomKind
import com.Pacvazo7.Rilcer.domain.model.RunOutcome

@Composable
fun ExplorationTurnScreen(
    roomIndex: Int,
    onBackToMap: () -> Unit,
    onSummary: (RunOutcome) -> Unit
) {
    val viewModel: ExplorationTurnViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(roomIndex) {
        viewModel.prepare(roomIndex)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                ExplorationEvent.BackToMap -> onBackToMap()
                is ExplorationEvent.ShowSummary -> onSummary(event.outcome)
            }
        }
    }

    ExplorationContent(
        state = state,
        onDecide = viewModel::decide,
        onProceed = viewModel::proceed
    )
}

@Composable
private fun ExplorationContent(
    state: ExplorationTurnUiState,
    onDecide: (Decision) -> Unit,
    onProceed: () -> Unit
) {
    val accent = accentFor(state.kind)
    val overlay = Brush.radialGradient(
        colors = listOf(
            accent.copy(alpha = 0.26f),
            BackgroundDeep.copy(alpha = 0.95f),
            BackgroundDeep.copy(alpha = 0.98f)
        )
    )

    VaultBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.title_exploration),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.weight(1f))

            RoomPlate(state = state, accent = accent)

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedContent(
                targetState = state.resolved,
                transitionSpec = {
                    fadeIn(tween(240)) togetherWith fadeOut(tween(200))
                },
                label = "decisionSwap"
            ) { resolved ->
                if (resolved) {
                    OutcomePanel(state = state, accent = accent, onProceed = onProceed)
                } else {
                    DecisionPanel(state = state, onDecide = onDecide)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RoomPlate(state: ExplorationTurnUiState, accent: Color) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(18.dp, shape, spotColor = Color.Black)
            .background(SurfaceRaised, shape)
            .border(2.dp, accent.copy(alpha = 0.75f), shape)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(104.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state.kind) {
                RoomKind.TRAP -> Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_encounter),
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )

                RoomKind.CACHE -> Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_encounter),
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )

                RoomKind.CORE -> Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_encounter),
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )

                RoomKind.CLUE -> Icon(
                    painter = painterResource(R.drawable.ic_clue),
                    contentDescription = stringResource(R.string.cd_encounter),
                    modifier = Modifier.size(72.dp),
                    tint = GoldLight
                )

                RoomKind.EMPTY -> Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(StoneLine, RoundedCornerShape(9.dp))
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(Labels.roomName(state.kind, state.trap)),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Labels.roomDescription(state.kind, state.trap)),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DecisionPanel(
    state: ExplorationTurnUiState,
    onDecide: (Decision) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.options.forEach { option ->
            SecondaryButton(
                textRes = when (option.decision) {
                    Decision.USE_TOOL -> R.string.action_use_tool
                    Decision.PUSH_ON -> R.string.action_push_on
                    Decision.FALL_BACK -> R.string.action_fall_back
                },
                onClick = { onDecide(option.decision) },
                modifier = Modifier.fillMaxWidth(),
                enabled = option.enabled,
                accent = if (option.decision == Decision.USE_TOOL) Malachite else Gold
            )
            val tool = option.tool
            Text(
                text = when {
                    option.blockedByFatigue && tool != null -> stringResource(
                        R.string.decision_fatigued,
                        stringResource(Labels.toolName(tool))
                    )

                    tool != null && !option.enabled -> stringResource(
                        R.string.decision_missing_tool,
                        stringResource(Labels.toolName(tool))
                    )

                    tool != null -> stringResource(
                        R.string.decision_tool_cost,
                        stringResource(Labels.toolName(tool))
                    )

                    option.cost > 0 -> stringResource(R.string.decision_cost, option.cost)
                    else -> stringResource(R.string.decision_free)
                },
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun OutcomePanel(
    state: ExplorationTurnUiState,
    accent: Color,
    onProceed: () -> Unit
) {
    val outcome = state.outcome
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlassCard(contentPadding = 14.dp) {
            Text(
                text = if (outcome == null) {
                    stringResource(R.string.outcome_empty_gallery)
                } else {
                    outcomeText(outcome)
                },
                style = MaterialTheme.typography.bodyLarge,
                color = accent,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            val artifact = outcome?.artifactGained
            if (artifact != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(
                        R.string.outcome_artifact,
                        stringResource(Labels.artifactName(artifact))
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = GoldLight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            DeltaLines(
                suppliesDelta = state.suppliesDelta,
                supplies = state.supplies,
                crystalsDelta = state.crystalsDelta,
                crystals = state.crystals
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            textRes = if (state.runEnded) R.string.action_see_summary else R.string.action_go_on,
            onClick = onProceed,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DeltaLines(
    suppliesDelta: Int,
    supplies: Int,
    crystalsDelta: Int,
    crystals: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.delta_supplies, supplies, suppliesDelta),
            style = MaterialTheme.typography.labelSmall,
            color = if (suppliesDelta < 0) Ember else TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        if (crystalsDelta != 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.delta_crystals, crystals, crystalsDelta),
                style = MaterialTheme.typography.labelSmall,
                color = Malachite,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun outcomeText(outcome: EncounterOutcome): String = when (outcome.kind) {
    OutcomeKind.SURVEY_VAGUE -> stringResource(
        Labels.outcomeTitle(outcome.kind),
        stringResource(Labels.heading(outcome.heading ?: Heading.NORTH))
    )

    OutcomeKind.BRACE_HOLDS,
    OutcomeKind.SEAL_PICKED,
    OutcomeKind.EMPTY_GALLERY -> stringResource(Labels.outcomeTitle(outcome.kind))

    else -> stringResource(Labels.outcomeTitle(outcome.kind), outcome.amount)
}

private fun accentFor(kind: RoomKind): Color = when (kind) {
    RoomKind.TRAP -> Ember
    RoomKind.CACHE -> Malachite
    RoomKind.CORE -> GoldLight
    RoomKind.CLUE -> Gold
    RoomKind.EMPTY -> StoneLine
}

