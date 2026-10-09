package com.dwan.colombia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = FlagBlueLight,
    onPrimary = Color.White,
    secondary = FlagGold,
    onSecondary = Color.Black,
    tertiary = FlagRed,
    onTertiary = Color.White,
    background = SurfaceDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerDark,
    onSurfaceVariant = Color(0xFFB8C0CC),
    primaryContainer = Color(0xFF163A6E),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondaryContainer = Color(0xFF4A3F00),
    onSecondaryContainer = FlagGold,
    error = FlagRed
)

private val LightColorScheme = lightColorScheme(
    primary = FlagBlue,
    onPrimary = Color.White,
    secondary = FlagGoldDeep,
    onSecondary = Color.Black,
    tertiary = FlagRed,
    onTertiary = Color.White,
    background = SurfaceLight,
    onBackground = OnSurfaceLight,
    surface = Color.White,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceContainerLight,
    onSurfaceVariant = Color(0xFF4A5565),
    primaryContainer = Color(0xFFD6E4FF),
    onPrimaryContainer = FlagBlue,
    secondaryContainer = Color(0xFFFFF3C4),
    onSecondaryContainer = Color(0xFF3D3200),
    error = FlagRed
)

@Composable
fun ColombiaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
