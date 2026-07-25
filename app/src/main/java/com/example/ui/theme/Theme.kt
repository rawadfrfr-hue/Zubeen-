package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MinimalColorScheme =
  lightColorScheme(
    primary = MinimalEqualsBg,
    onPrimary = MinimalEqualsText,
    secondary = MinimalOperatorBg,
    onSecondary = MinimalOperatorText,
    secondaryContainer = MinimalOperatorBg,
    onSecondaryContainer = MinimalOperatorText,
    tertiary = MinimalActionBg,
    onTertiary = MinimalActionText,
    tertiaryContainer = MinimalActionBg,
    onTertiaryContainer = MinimalActionText,
    background = MinimalBackground,
    onBackground = MinimalTextPrimary,
    surface = MinimalBackground,
    onSurface = MinimalTextPrimary,
    surfaceVariant = MinimalSurfaceContainer,
    onSurfaceVariant = MinimalTextSecondary,
    surfaceContainer = MinimalSurfaceContainer,
    surfaceContainerHigh = MinimalActionBg,
    outline = MinimalOutline,
    outlineVariant = MinimalOutline
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = MinimalColorScheme, typography = Typography, content = content)
}
