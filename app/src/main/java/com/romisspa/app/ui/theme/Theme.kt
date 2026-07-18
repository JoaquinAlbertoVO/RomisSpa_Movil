package com.romisspa.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SpaLightColorScheme = lightColorScheme(
    primary          = RoseGold,
    onPrimary        = White,
    primaryContainer = RoseGoldLight,
    onPrimaryContainer = CharcoalSoft,
    secondary        = WarmGold,
    onSecondary      = White,
    secondaryContainer = WarmGoldLight,
    onSecondaryContainer = CharcoalSoft,
    background       = Cream,
    onBackground     = CharcoalSoft,
    surface          = White,
    onSurface        = CharcoalSoft,
    surfaceVariant   = CreamDark,
    onSurfaceVariant = GreyWarm,
    error            = ErrorRed,
    outline          = RoseGoldLight
)

private val SpaDarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary          = RoseGoldDarkTheme,
    onPrimary        = CharcoalDeep,
    primaryContainer = RoseGoldDark,
    onPrimaryContainer = TextLight,
    secondary        = WarmGoldDarkTheme,
    onSecondary      = CharcoalDeep,
    secondaryContainer = WarmGold,
    onSecondaryContainer = TextLight,
    background       = CharcoalDeep,
    onBackground     = TextLight,
    surface          = CharcoalSurface,
    onSurface        = TextLight,
    surfaceVariant   = CharcoalVariant,
    onSurfaceVariant = TextMuted,
    error            = ErrorRed,
    outline          = CharcoalSoft
)

@Composable
fun RomisSpaTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SpaDarkColorScheme else SpaLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = SpaTypography,
        content     = content
    )
}
