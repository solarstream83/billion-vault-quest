package com.Pacvazo7.Rilcer.presentation.menu

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.Malachite
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.BrassChip
import com.Pacvazo7.Rilcer.core.ui.Labels
import com.Pacvazo7.Rilcer.core.ui.PrimaryButton
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton
import com.Pacvazo7.Rilcer.core.ui.StatCardData
import com.Pacvazo7.Rilcer.core.ui.StatRow
import com.Pacvazo7.Rilcer.core.ui.VaultBackground
import com.Pacvazo7.Rilcer.presentation.dialog.HowToPlayDialog

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onWorkshop: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory.factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    var rulesVisible by remember { mutableStateOf(false) }

    MenuContent(
        state = state,
        onPlay = onPlay,
        onWorkshop = onWorkshop,
        onCollection = onCollection,
        onSettings = onSettings,
        onRules = { rulesVisible = true }
    )

    if (rulesVisible) {
        HowToPlayDialog(onDismiss = { rulesVisible = false })
    }
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onWorkshop: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit,
    onRules: () -> Unit
) {
    val overlay = Brush.verticalGradient(
        0f to BackgroundDeep.copy(alpha = 0.25f),
        0.52f to BackgroundDeep.copy(alpha = 0.55f),
        0.68f to BackgroundDeep.copy(alpha = 0.88f),
        1f to BackgroundDeep.copy(alpha = 0.98f)
    )
    val transition = rememberInfiniteTransition(label = "menu")
    val breath by transition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoBreath"
    )
    val logoAlpha = if (state.reducedMotion) 1f else breath

    VaultBackground(overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.36f)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(128.dp)
                        .alpha(logoAlpha),
                    contentScale = ContentScale.Fit
                )
                BrassChip(
                    imageRes = R.drawable.sprite_3,
                    value = state.crystals.toString(),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                )
            }

            MenuSheet(
                modifier = Modifier.weight(1f),
                state = state,
                onPlay = onPlay,
                onWorkshop = onWorkshop,
                onCollection = onCollection,
                onSettings = onSettings,
                onRules = onRules
            )
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onWorkshop: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit,
    onRules: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(20.dp, shape, spotColor = Color.Black)
            .background(
                Brush.verticalGradient(
                    listOf(SurfaceBase.copy(alpha = 0.95f), BackgroundDeep.copy(alpha = 0.99f))
                ),
                shape
            )
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(4.dp)
                .background(Gold.copy(alpha = 0.55f), RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.app_title),
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.menu_tagline),
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(14.dp))

        StatRow(
            cards = listOf(
                StatCardData(
                    labelRes = R.string.stat_depth_record,
                    amount = state.depthRecord,
                    display = state.depthRecord.toString(),
                    accent = Gold
                ),
                StatCardData(
                    labelRes = R.string.stat_artifacts,
                    amount = state.artifactsOwned,
                    display = stringResource(
                        R.string.value_fraction,
                        state.artifactsOwned,
                        state.artifactsTotal
                    ),
                    accent = Malachite,
                    imageRes = R.drawable.sprite_5
                ),
                StatCardData(
                    labelRes = R.string.stat_expeditions,
                    amount = state.expeditions,
                    display = state.expeditions.toString(),
                    accent = GoldLight
                )
            )
        )

        val objective = state.objective
        if (objective != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    R.string.menu_objective,
                    stringResource(Labels.objectiveName(objective.id)),
                    objective.current,
                    objective.target
                ),
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.menu_hint),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        PrimaryButton(
            textRes = R.string.action_play,
            onClick = onPlay,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = R.drawable.ic_lantern
        )
        Spacer(modifier = Modifier.height(12.dp))
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
                textRes = R.string.action_collection,
                onClick = onCollection,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                textRes = R.string.action_settings,
                onClick = onSettings,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                textRes = R.string.action_how_to_play,
                onClick = onRules,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.menu_disclaimer),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}
