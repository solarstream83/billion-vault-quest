package com.Pacvazo7.Rilcer.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Pacvazo7.Rilcer.R
import com.Pacvazo7.Rilcer.core.theme.CacheSurface
import com.Pacvazo7.Rilcer.core.theme.CollapseBar
import com.Pacvazo7.Rilcer.core.theme.Ember
import com.Pacvazo7.Rilcer.core.theme.Gold
import com.Pacvazo7.Rilcer.core.theme.GoldLight
import com.Pacvazo7.Rilcer.core.theme.Malachite
import com.Pacvazo7.Rilcer.core.theme.StoneLine
import com.Pacvazo7.Rilcer.core.theme.SurfaceWarm
import com.Pacvazo7.Rilcer.core.theme.TileBorder
import com.Pacvazo7.Rilcer.core.theme.TileCollapsed
import com.Pacvazo7.Rilcer.core.theme.TileEmpty
import com.Pacvazo7.Rilcer.core.theme.TileSealed
import com.Pacvazo7.Rilcer.core.theme.TrapSurface
import com.Pacvazo7.Rilcer.domain.model.Room
import com.Pacvazo7.Rilcer.domain.model.RoomKind

@Composable
fun VaultTile(
    room: Room,
    size: Dp,
    selected: Boolean,
    hasTeam: Boolean,
    reducedMotion: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    val transition = rememberInfiniteTransition(label = "tilePulse")
    val pulse by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tileCandidate"
    )
    val selectScale by animateFloatAsState(
        targetValue = if (selected) 1.04f else 1f,
        label = "tileSelect"
    )
    val revealAlpha by animateFloatAsState(
        targetValue = if (room.revealed) 1f else 0.9f,
        animationSpec = tween(320, easing = FastOutSlowInEasing),
        label = "tileReveal"
    )

    val candidateGlow = if (reducedMotion) 0.7f else pulse
    val surface: Color = when {
        room.collapsed -> TileCollapsed
        !room.revealed -> TileSealed
        room.kind == RoomKind.TRAP -> TrapSurface
        room.kind == RoomKind.CACHE -> CacheSurface
        room.kind == RoomKind.CLUE -> SurfaceWarm
        room.kind == RoomKind.CORE -> SurfaceWarm
        else -> TileEmpty
    }
    val borderColor: Color = when {
        room.collapsed -> CollapseBar
        selected -> GoldLight
        room.candidate && !room.revealed -> Gold.copy(alpha = candidateGlow)
        room.revealed && room.kind == RoomKind.TRAP -> Ember
        room.revealed && room.kind == RoomKind.CACHE -> Malachite
        room.revealed && room.kind == RoomKind.CORE -> GoldLight
        else -> TileBorder
    }
    val stateRes = when {
        room.collapsed -> R.string.tile_state_collapsed
        hasTeam -> R.string.tile_state_team
        selected -> R.string.tile_state_selected
        room.candidate && !room.revealed -> R.string.tile_state_candidate
        room.revealed -> R.string.tile_state_open
        else -> R.string.tile_state_sealed
    }
    val stateLabel = stringResource(stateRes)
    val tileLabel = stringResource(R.string.cd_vault_room)

    Box(
        modifier = modifier
            .size(size)
            .padding(1.5.dp)
            .scale(selectScale)
            .alpha(revealAlpha)
            .background(surface, shape)
            .border(if (selected) 2.dp else 1.dp, borderColor, shape)
            .clickable(enabled = !room.collapsed, onClick = onClick)
            .semantics {
                contentDescription = tileLabel
                stateDescription = stateLabel
            },
        contentAlignment = Alignment.Center
    ) {
        if (room.revealed && room.kind == RoomKind.CORE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(GoldLight.copy(alpha = 0.35f), Color.Transparent)
                        ),
                        shape
                    )
            )
        }
        if (hasTeam) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(Malachite.copy(alpha = 0.30f), Color.Transparent)
                        ),
                        shape
                    )
            )
        }
        when {
            room.collapsed -> CollapsedMark(size)
            !room.revealed -> Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = null,
                modifier = Modifier
                    .size(size * 0.54f)
                    .alpha(if (room.candidate) 1f else 0.78f),
                contentScale = ContentScale.Fit
            )

            room.kind == RoomKind.CLUE -> Icon(
                painter = painterResource(R.drawable.ic_clue),
                contentDescription = null,
                modifier = Modifier.size(size * 0.42f),
                tint = GoldLight
            )

            room.kind == RoomKind.TRAP -> Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = null,
                modifier = Modifier.size(size * 0.54f),
                contentScale = ContentScale.Fit
            )

            room.kind == RoomKind.CACHE -> Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                modifier = Modifier
                    .size(size * 0.54f)
                    .alpha(if (room.looted) 0.45f else 1f),
                contentScale = ContentScale.Fit
            )

            room.kind == RoomKind.CORE -> Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                modifier = Modifier.size(size * 0.58f),
                contentScale = ContentScale.Fit
            )

            else -> Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(StoneLine, RoundedCornerShape(5.dp))
            )
        }
        if (hasTeam) {
            Image(
                painter = painterResource(R.drawable.sprite_1),
                contentDescription = null,
                modifier = Modifier
                    .size(size * 0.62f)
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun CollapsedMark(size: Dp) {
    Canvas(modifier = Modifier.size(size * 0.5f)) {
        val stroke = 2.dp.toPx()
        drawLine(
            color = CollapseBar,
            start = Offset(0f, 0f),
            end = Offset(this.size.width, this.size.height),
            strokeWidth = stroke
        )
        drawLine(
            color = CollapseBar,
            start = Offset(this.size.width, 0f),
            end = Offset(0f, this.size.height),
            strokeWidth = stroke
        )
    }
}
