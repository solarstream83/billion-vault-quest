package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceWarm
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.domain.model.ToolId

@Composable
fun ToolRow(
    tool: ToolId,
    level: Int,
    cost: Int,
    affordable: Boolean,
    mastered: Boolean,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    val stateLabel = stringResource(R.string.tool_state_level, level, GameConfig.TOOL_MAX_LEVEL)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(Brush.verticalGradient(listOf(SurfaceWarm, SurfaceBase)), shape)
            .border(1.dp, Gold.copy(alpha = 0.35f), shape)
            .semantics { stateDescription = stateLabel }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Labels.toolIcon(tool)),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = GoldLight
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Labels.toolName(tool)),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(GameConfig.TOOL_MAX_LEVEL) { pip ->
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    if (pip < level) Gold else StoneLine,
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(Labels.toolEffect(tool)),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                maxLines = 2
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        if (mastered) {
            Text(
                text = stringResource(R.string.tool_mastered),
                style = MaterialTheme.typography.labelSmall,
                color = GoldLight,
                modifier = Modifier.width(78.dp)
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(96.dp)
            ) {
                SecondaryButton(
                    textRes = R.string.action_upgrade,
                    onClick = onUpgrade,
                    enabled = affordable,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        if (affordable) R.string.tool_cost else R.string.tool_need,
                        cost
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (affordable) GoldLight else TextMuted,
                    maxLines = 1
                )
            }
        }
    }
}
