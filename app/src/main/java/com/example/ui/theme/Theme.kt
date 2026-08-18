package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentTealGlow,
    onPrimary = DarkTealSecondary,
    primaryContainer = DarkTealSecondary,
    onPrimaryContainer = MintContainer,
    secondary = DeepTealLight,
    onSecondary = Color.White,
    tertiary = MintContainer,
    onTertiary = DarkTealSecondary,
    background = Color(0xFF001B1A),
    onBackground = Color(0xFFE0F2F1),
    surface = Color(0xFF002827),
    onSurface = Color(0xFFE0F2F1),
    surfaceVariant = Color(0xFF003735),
    onSurfaceVariant = Color(0xFFA0C2BF),
    outline = Color(0xFF004D4A)
)

private val LightColorScheme = lightColorScheme(
    primary = DeepTealPrimary,
    onPrimary = Color.White,
    primaryContainer = MintContainer,
    onPrimaryContainer = OnMintContainer,
    secondary = DarkTealSecondary,
    onSecondary = Color.White,
    tertiary = DeepTealLight,
    onTertiary = Color.White,
    background = SleekBackgroundLight,
    onBackground = SleekTextDark,
    surface = SleekSurfaceLight,
    onSurface = SleekTextDark,
    surfaceVariant = SleekSurfaceVariant,
    onSurfaceVariant = SleekTextMuted,
    outline = SleekBorder
)

@Composable
fun FocusBrainTheme(
    darkTheme: Boolean = false, // Default to Sleek Interface Light Theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

