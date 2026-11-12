package com.Pacvazo7.Rilcer.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.core.di.AppViewModelFactory
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextOnGold
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.ScreenScaffold
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.SectionHeader
import com.Pacvazo7.Rilcer.presentation.dialog.ConfirmDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onBack = onBack,
        onCols = viewModel::chooseCols,
        onPrecision = viewModel::choosePrecision,
        onSupplies = viewModel::chooseSupplies,
        onMotion = viewModel::toggleReducedMotion,
        onAskReset = viewModel::askReset,
        onDismissReset = viewModel::dismissReset,
        onConfirmReset = viewModel::confirmReset
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onCols: (Int) -> Unit,
    onPrecision: (Boolean) -> Unit,
    onSupplies: (Int) -> Unit,
    onMotion: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onDismissReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    val overlay = Brush.verticalGradient(
        listOf(SurfaceBase.copy(alpha = 0.93f), BackgroundDeep.copy(alpha = 0.97f))
    )
    ScreenScaffold(
        titleRes = R.string.title_settings,
        overlay = overlay,
        onBack = onBack
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SectionHeader(titleRes = R.string.section_expedition)

                SettingRow(labelRes = R.string.setting_vault_size) {
                    SegmentOption(
                        labelRes = R.string.option_standard,
                        selected = state.cols == GameConfig.GRID_STANDARD,
                        onClick = { onCols(GameConfig.GRID_STANDARD) }
                    )
                    SegmentOption(
                        labelRes = R.string.option_deep,
                        selected = state.cols == GameConfig.GRID_DEEP,
                        onClick = { onCols(GameConfig.GRID_DEEP) }
                    )
                }

                SettingRow(labelRes = R.string.setting_clue_detail) {
                    SegmentOption(
                        labelRes = R.string.option_precise,
                        selected = state.preciseClues,
                        onClick = { onPrecision(true) }
                    )
                    SegmentOption(
                        labelRes = R.string.option_vague,
                        selected = !state.preciseClues,
                        onClick = { onPrecision(false) }
                    )
                }

                SettingRow(labelRes = R.string.setting_supplies) {
                    SegmentOption(
                        labelRes = R.string.option_supplies_lean,
                        selected = state.startingSupplies == GameConfig.LEAN_SUPPLIES,
                        onClick = { onSupplies(GameConfig.LEAN_SUPPLIES) }
                    )
                    SegmentOption(
                        labelRes = R.string.option_supplies_standard,
                        selected = state.startingSupplies == GameConfig.DEFAULT_SUPPLIES,
                        onClick = { onSupplies(GameConfig.DEFAULT_SUPPLIES) }
                    )
                    SegmentOption(
                        labelRes = R.string.option_supplies_rich,
                        selected = state.startingSupplies == GameConfig.RICH_SUPPLIES,
                        onClick = { onSupplies(GameConfig.RICH_SUPPLIES) }
                    )
                }

                SectionHeader(titleRes = R.string.section_presentation, accent = GoldLight)
                MotionRow(enabled = state.reducedMotion, onMotion = onMotion)

                SectionHeader(titleRes = R.string.section_progress, accent = Ember)
                TextButton(onClick = onAskReset, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.action_reset_progress),
                        style = MaterialTheme.typography.titleMedium,
                        color = Ember
                    )
                }
                if (state.resetDone) {
                    Text(
                        text = stringResource(R.string.settings_reset_done),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            SecondaryButton(
                textRes = R.string.action_menu,
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = R.drawable.icon_home
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (state.confirmResetVisible) {
        ConfirmDialog(
            titleRes = R.string.dialog_reset_title,
            messageRes = R.string.dialog_reset_message,
            confirmRes = R.string.action_reset,
            dismissRes = R.string.action_cancel,
            onConfirm = onConfirmReset,
            onDismiss = onDismissReset
        )
    }
}

@Composable
private fun SettingRow(
    labelRes: Int,
    options: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(SurfaceRaised.copy(alpha = 0.92f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options()
        }
    }
}

@Composable
private fun SegmentOption(
    labelRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val label = stringResource(
        if (selected) R.string.option_state_selected else R.string.option_state_idle
    )
    Box(
        modifier = Modifier
            .height(48.dp)
            .background(if (selected) Gold else StoneLine.copy(alpha = 0.35f), shape)
            .border(1.dp, if (selected) GoldLight else TextMuted.copy(alpha = 0.3f), shape)
            .clickable(onClick = onClick)
            .semantics { stateDescription = label }
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) TextOnGold else TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun MotionRow(
    enabled: Boolean,
    onMotion: (Boolean) -> Unit
) {
    val label = stringResource(
        if (enabled) R.string.option_state_selected else R.string.option_state_idle
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(SurfaceRaised.copy(alpha = 0.92f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .semantics { stateDescription = label },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.setting_reduced_motion),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = stringResource(R.string.setting_reduced_motion_hint),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        Switch(
            checked = enabled,
            onCheckedChange = onMotion,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TextOnGold,
                checkedTrackColor = Gold,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = StoneLine
            )
        )
    }
}
