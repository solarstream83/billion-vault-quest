package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.config.GameConfig
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.TextMuted

@Composable
fun SupplyMeter(
    current: Int,
    max: Int,
    modifier: Modifier = Modifier
) {
    val low = current <= GameConfig.LOW_SUPPLY_WARNING
    val accent = if (low) Ember else Gold
    val label = stringResource(R.string.supply_state, current, max)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = label
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.stat_supplies),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .background(StoneLine.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
        ) {
            val ratio = if (max <= 0) 0f else (current.toFloat() / max.toFloat()).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(ratio)
                    .height(6.dp)
                    .background(
                        Brush.horizontalGradient(listOf(GoldLight, accent)),
                        RoundedCornerShape(3.dp)
                    )
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = current.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = accent
        )
    }
}
