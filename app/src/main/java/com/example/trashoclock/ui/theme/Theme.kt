package com.example.trashoclock.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// The design is light-only, so dynamic color and dark mode are not used:
// they would replace the Figma palette.
private val LightColorScheme = lightColorScheme(
    primary = Green,
    secondary = GreenMuted,
    onBackground = GreenDark,
    onSurface = GreenDark,
)

@Composable
fun TrashOClockTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
