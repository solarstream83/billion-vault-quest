package com.Pacvazo7.Rilcer.presentation.collection

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
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.ui.ArtifactSlot
import com.Pacvazo7.Rilcer.core.ui.BrassChip
import com.Pacvazo7.Rilcer.core.ui.GlassCard
import com.Pacvazo7.Rilcer.core.ui.Labels
import com.Pacvazo7.Rilcer.core.ui.ScreenScaffold
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton

@Composable
fun ArtifactCollectionScreen(onBack: () -> Unit) {
    val viewModel: ArtifactCollectionViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ArtifactCollectionContent(state = state, onBack = onBack)
}

@Composable
private fun ArtifactCollectionContent(
    state: ArtifactCollectionUiState,
    onBack: () -> Unit
) {
    val overlay = Brush.verticalGradient(
        listOf(BackgroundDeep.copy(alpha = 0.90f), SurfaceBase.copy(alpha = 0.94f))
    )
    ScreenScaffold(
        titleRes = R.string.title_collection,
        overlay = overlay,
        onBack = onBack,
        trailing = {
            BrassChip(
                imageRes = R.drawable.sprite_5,
                value = stringResource(R.string.value_fraction, state.ownedCount, state.total)
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if (state.empty) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.collection_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                state.sets.forEach { set ->
                    GlassCard(contentPadding = 14.dp) {
                        Text(
                            text = stringResource(Labels.artifactSetName(set.id)),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (set.complete) GoldLight else TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            set.artifacts.forEach { artifact ->
                                ArtifactSlot(
                                    artifactId = artifact.id,
                                    owned = set.owned.contains(artifact.id),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        if (set.complete) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(Labels.artifactSetBonus(set.id)),
                                style = MaterialTheme.typography.labelSmall,
                                color = GoldLight
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
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
