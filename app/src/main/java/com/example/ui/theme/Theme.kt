package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HexamineDarkColorScheme = darkColorScheme(
    primary = HexGold,
    onPrimary = Color.Black,
    primaryContainer = HexamineCardElevated,
    onPrimaryContainer = HexGoldBright,
    secondary = HexTeal,
    onSecondary = Color.Black,
    secondaryContainer = HexamineCard,
    onSecondaryContainer = HexTeal,
    tertiary = HexBlue,
    onTertiary = Color.White,
    background = HexamineBg,
    onBackground = HexTextPrimary,
    surface = HexamineSurface,
    onSurface = HexTextPrimary,
    surfaceVariant = HexamineCard,
    onSurfaceVariant = HexTextSecondary,
    outline = HexamineBorder,
    outlineVariant = HexamineBorderBright,
    error = HexRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // We enforce the immersive Hexamine dark cyber theme for consistent industrial mining experience
    MaterialTheme(
        colorScheme = HexamineDarkColorScheme,
        typography = Typography,
        content = content
    )
}
