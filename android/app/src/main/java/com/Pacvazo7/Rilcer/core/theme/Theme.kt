package com.Pacvazo7.Rilcer.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VaultColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = TextOnGold,
    primaryContainer = SurfaceWarm,
    onPrimaryContainer = GoldLight,
    secondary = Malachite,
    onSecondary = BackgroundDeep,
    tertiary = GoldLight,
    onTertiary = TextOnGold,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceBase,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = GoldDeep,
    outlineVariant = TileBorder,
    error = Ember,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VaultColorScheme,
        typography = VaultTypography,
        content = content
    )
}
