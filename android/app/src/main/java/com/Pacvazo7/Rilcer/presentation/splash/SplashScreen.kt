package com.Pacvazo7.Rilcer.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import com.Pacvazo7.Rilcer.core.theme.BackgroundBlack
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldDeep
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceWarm
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.VaultBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory.factory)
    val finished by viewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) onFinished()
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val overlay = Brush.verticalGradient(
        0f to BackgroundDeep.copy(alpha = 0.94f),
        0.5f to BackgroundDeep.copy(alpha = 0.90f),
        1f to BackgroundBlack.copy(alpha = 0.97f)
    )
    val transition = rememberInfiniteTransition(label = "splash")
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep"
    )
    val ring by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.70f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring"
    )
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "drift"
    )

    VaultBackground(overlay = overlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Gold.copy(alpha = 0.14f), Color.Transparent),
                        radius = 420f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(256.dp)
                        .scale(1f + (ring - 0.35f) * 0.18f)
                        .alpha(ring)
                        .border(1.dp, GoldDeep.copy(alpha = 0.7f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .shadow(18.dp, CircleShape, spotColor = BackgroundBlack)
                        .background(
                            Brush.radialGradient(listOf(SurfaceWarm, BackgroundDeep)),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .rotate(sweep)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(Gold, GoldDeep, GoldLight, GoldDeep, Gold)
                            ),
                            shape = CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(168.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.app_title_line_one),
                style = MaterialTheme.typography.displaySmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.app_title_line_two),
                style = MaterialTheme.typography.displaySmall,
                color = GoldLight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(44.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp),
                color = Gold,
                trackColor = SurfaceBase
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.34f)
                .align(Alignment.BottomCenter)
        ) {
            DustMotes(drift)
        }
    }
}

@Composable
private fun DustMotes(phase: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent, RoundedCornerShape(0.dp))
    ) {
        val width = size.width
        val height = size.height
        val lanes = 6
        for (lane in 0 until lanes) {
            val laneOffset = (lane + 1f) / (lanes + 1f)
            val local = (phase + lane * 0.17f) % 1f
            val y = height - local * height
            val fade = 1f - local
            drawCircle(
                color = GoldLight.copy(alpha = 0.10f + 0.32f * fade),
                radius = 3f + lane % 3,
                center = Offset(width * laneOffset, y)
            )
        }
    }
}
