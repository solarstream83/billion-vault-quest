package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Dp = 18.dp,
    contentPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(corner)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, shape, spotColor = Color.Black, ambientColor = Color.Black)
            .background(
                brush = Brush.verticalGradient(
                    listOf(SurfaceRaised.copy(alpha = 0.94f), SurfaceBase.copy(alpha = 0.92f))
                ),
                shape = shape
            )
            .border(1.dp, Color.White.copy(alpha = 0.08f), shape)
            .padding(contentPadding),
        content = content
    )
}
