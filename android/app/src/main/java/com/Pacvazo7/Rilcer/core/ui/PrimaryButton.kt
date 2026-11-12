package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldDark
import com.Pacvazo7.Rilcer.core.theme.GoldHighlight
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.SurfaceDisabled
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextOnGold

@Composable
fun PrimaryButton(
    textRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Int? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "primaryPress"
    )
    val shape = RoundedCornerShape(16.dp)
    val surface = if (enabled) {
        Brush.horizontalGradient(listOf(GoldLight, Gold, GoldDark))
    } else {
        Brush.horizontalGradient(listOf(SurfaceDisabled, SurfaceDisabled))
    }
    Button(
        onClick = onClick,
        modifier = modifier
            .height(60.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 14.dp else 0.dp,
                shape = shape,
                spotColor = Gold.copy(alpha = 0.55f),
                ambientColor = Gold.copy(alpha = 0.35f)
            )
            .background(surface, shape)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        if (enabled) GoldHighlight.copy(alpha = 0.6f) else Color.Transparent,
                        Color.Transparent
                    )
                ),
                shape = shape
            ),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextOnGold,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextMuted
        ),
        elevation = null,
        contentPadding = PaddingValues(horizontal = 18.dp),
        interactionSource = interactionSource
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (enabled) TextOnGold else TextMuted
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.titleMedium,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
