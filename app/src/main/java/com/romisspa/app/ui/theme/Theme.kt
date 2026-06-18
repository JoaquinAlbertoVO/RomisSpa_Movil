package com.romisspa.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SpaColorScheme = lightColorScheme(
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

@Composable
fun RomisSpaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SpaColorScheme,
        typography  = SpaTypography,
        content     = content
    )
}
