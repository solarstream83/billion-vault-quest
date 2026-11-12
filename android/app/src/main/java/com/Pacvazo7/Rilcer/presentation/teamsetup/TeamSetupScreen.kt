package com.Pacvazo7.Rilcer.presentation.teamsetup

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.di.AppViewModelFactory
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.BrassChip
import com.Pacvazo7.Rilcer.core.ui.GlassCard
import com.Pacvazo7.Rilcer.core.ui.Labels
import com.Pacvazo7.Rilcer.core.ui.PrimaryButton
import com.Pacvazo7.Rilcer.core.ui.ScreenScaffold
import com.Pacvazo7.Rilcer.core.ui.SectionHeader
import com.Pacvazo7.Rilcer.core.ui.SpecialistCard
import com.Pacvazo7.Rilcer.core.ui.SpecialistChip
import com.Pacvazo7.Rilcer.domain.model.Specialist

@Composable
fun TeamSetupScreen(
    onBack: () -> Unit,
    onEnterVault: () -> Unit
) {
    val viewModel: TeamSetupViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                TeamSetupEvent.EnterVault -> onEnterVault()
            }
        }
    }

    TeamSetupContent(
        state = state,
        onBack = onBack,
        onSlot = viewModel::openSlot,
        onPick = viewModel::assign,
        onClear = viewModel::clearSlot,
        onBegin = viewModel::beginExpedition
    )
}

@Composable
private fun TeamSetupContent(
    state: TeamSetupUiState,
    onBack: () -> Unit,
    onSlot: (Int) -> Unit,
    onPick: (Specialist) -> Unit,
    onClear: (Int) -> Unit,
    onBegin: () -> Unit
) {
    val overlay = Brush.verticalGradient(
        listOf(BackgroundDeep.copy(alpha = 0.86f), SurfaceBase.copy(alpha = 0.95f))
    )
    ScreenScaffold(
        titleRes = R.string.title_team_setup,
        overlay = overlay,
        onBack = onBack,
        trailing = {
            BrassChip(
                imageRes = R.drawable.ic_supply,
                value = state.suppliesPreview.toString()
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SectionHeader(titleRes = R.string.section_team)
                state.slots.forEachIndexed { index, specialist ->
                    SpecialistCard(
                        specialist = specialist,
                        slotIndex = index,
                        opened = state.openSlot == index,
                        onClick = {
                            if (specialist != null && state.openSlot == index) {
                                onClear(index)
                            } else {
                                onSlot(index)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (state.openSlot != null) {
                    SectionHeader(titleRes = R.string.section_roster, accent = GoldLight)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        state.roster.forEach { candidate ->
                            val taken = state.slots.withIndex().any {
                                it.index != state.openSlot && it.value == candidate
                            }
                            SpecialistChip(
                                specialist = candidate,
                                enabled = !taken,
                                onClick = { onPick(candidate) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                GlassCard(contentPadding = 12.dp) {
                    Text(
                        text = stringResource(R.string.loadout_title),
                        style = MaterialTheme.typography.labelSmall,
                        color = Gold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.loadout_supplies, state.suppliesPreview),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                    if (state.assigned.isEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.loadout_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted
                        )
                    } else {
                        state.assigned.forEach { specialist ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    R.string.loadout_line,
                                    stringResource(Labels.specialistName(specialist)),
                                    stringResource(Labels.toolName(specialist.tool))
                                ),
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
                    }
                    state.setBonuses.forEach { set ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(Labels.artifactSetBonus(set)),
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldLight
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = stringResource(R.string.team_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            PrimaryButton(
                textRes = R.string.action_play,
                onClick = onBegin,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.ready,
                leadingIcon = R.drawable.ic_lantern
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
