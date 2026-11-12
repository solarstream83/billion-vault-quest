package com.Pacvazo7.Rilcer.presentation.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Pacvazo7.Rilcer.core.theme.BackgroundDeep
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised
import com.Pacvazo7.Rilcer.core.theme.TextPrimary
import com.Pacvazo7.Rilcer.core.theme.TextSecondary
import com.Pacvazo7.Rilcer.core.ui.SecondaryButton

@Composable
fun ConfirmDialog(
    titleRes: Int,
    messageRes: Int,
    confirmRes: Int,
    dismissRes: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        val shape = RoundedCornerShape(20.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(SurfaceRaised, BackgroundDeep)
                    ),
                    shape
                )
                .border(1.dp, Gold.copy(alpha = 0.45f), shape)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(messageRes),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    textRes = dismissRes,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    textRes = confirmRes,
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    accent = Ember
                )
            }
        }
    }
}
