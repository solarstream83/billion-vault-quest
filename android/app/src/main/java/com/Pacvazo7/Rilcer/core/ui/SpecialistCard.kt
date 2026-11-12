package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceBase
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.domain.model.Specialist

@Composable
fun SpecialistCard(
    specialist: Specialist?,
    slotIndex: Int,
    opened: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    val stateLabel = stringResource(
        if (specialist == null) R.string.slot_state_empty else R.string.slot_state_filled
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(
                Brush.verticalGradient(listOf(SurfaceRaised, SurfaceBase)),
                shape
            )
            .border(
                width = if (opened) 2.dp else 1.dp,
                color = if (opened) GoldLight else Gold.copy(alpha = 0.30f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .semantics { stateDescription = stateLabel }
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    if (specialist == null) StoneLine.copy(alpha = 0.4f) else Gold.copy(alpha = 0.16f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (specialist == null) {
                Text(
                    text = (slotIndex + 1).toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextMuted
                )
            } else {
                Icon(
                    painter = painterResource(Labels.toolIcon(specialist.tool)),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = GoldLight
                )
            }
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            if (specialist == null) {
                Text(
                    text = stringResource(R.string.slot_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextMuted,
                    textAlign = TextAlign.Start
                )
            } else {
                Text(
                    text = stringResource(Labels.specialistName(specialist)),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(Labels.specialistEffect(specialist)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun SpecialistChip(
    specialist: Specialist,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    val stateLabel = stringResource(
        if (enabled) R.string.chip_state_available else R.string.chip_state_taken
    )
    Column(
        modifier = modifier
            .width(104.dp)
            .background(
                if (enabled) SurfaceRaised else StoneLine.copy(alpha = 0.35f),
                shape
            )
            .border(1.dp, if (enabled) Gold.copy(alpha = 0.4f) else Color.Transparent, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .semantics { stateDescription = stateLabel }
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Labels.toolIcon(specialist.tool)),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = if (enabled) GoldLight else TextMuted
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(Labels.specialistName(specialist)),
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) TextPrimary else TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Text(
            text = stringResource(Labels.toolName(specialist.tool)),
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) TextSecondary else TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
