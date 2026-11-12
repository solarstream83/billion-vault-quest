package com.Pacvazo7.Rilcer.presentation.workshop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.Pacvazo7.Rilcer.core.theme.SurfaceWarm
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.BrassChip
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.SectionHeader
import com.Pacvazo7.Rilcer.core.ui.ScreenScaffold
import com.Pacvazo7.Rilcer.core.ui.ToolRow
import com.Pacvazo7.Rilcer.domain.model.ToolId

@Composable
fun ToolWorkshopScreen(onBack: () -> Unit) {
    val viewModel: ToolWorkshopViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ToolWorkshopContent(
        state = state,
        onBack = onBack,
        onUpgrade = viewModel::upgrade
    )
}

@Composable
private fun ToolWorkshopContent(
    state: ToolWorkshopUiState,
    onBack: () -> Unit,
    onUpgrade: (ToolId) -> Unit
) {
    val overlay = Brush.verticalGradient(
        listOf(SurfaceWarm.copy(alpha = 0.90f), BackgroundDeep.copy(alpha = 0.96f))
    )
    ScreenScaffold(
        titleRes = R.string.title_workshop,
        overlay = overlay,
        onBack = onBack,
        trailing = {
            BrassChip(
                imageRes = R.drawable.sprite_3,
                value = state.crystals.toString()
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                SectionHeader(titleRes = R.string.section_tools)
                state.rows.forEach { row ->
                    ToolRow(
                        tool = row.tool,
                        level = row.level,
                        cost = row.cost,
                        affordable = row.affordable,
                        mastered = row.mastered,
                        onUpgrade = { onUpgrade(row.tool) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                if (state.allMastered) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.workshop_all_mastered),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
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
}
