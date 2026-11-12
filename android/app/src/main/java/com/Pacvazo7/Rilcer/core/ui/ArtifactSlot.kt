package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceRaised
import com.Pacvazo7.Rilcer.core.theme.TextMuted
import com.Pacvazo7.Rilcer.core.theme.TextPrimary

@Composable
fun ArtifactSlot(
    artifactId: Int,
    owned: Boolean,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val stateLabel = stringResource(
        if (owned) R.string.artifact_state_owned else R.string.artifact_state_missing
    )
    Column(
        modifier = modifier.semantics { stateDescription = stateLabel },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .background(SurfaceRaised.copy(alpha = 0.9f), shape)
                .border(
                    1.dp,
                    if (owned) GoldLight.copy(alpha = 0.6f) else StoneLine.copy(alpha = 0.5f),
                    shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .alpha(if (owned) 1f else 0.18f),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(
                if (owned) Labels.artifactName(artifactId) else R.string.artifact_hidden
            ),
            style = MaterialTheme.typography.labelSmall,
            color = if (owned) TextPrimary else TextMuted,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
