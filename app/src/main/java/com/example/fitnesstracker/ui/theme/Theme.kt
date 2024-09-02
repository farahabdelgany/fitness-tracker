package com.example.fitnesstracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CustomPrimary,
    onPrimary = Color.White,
    secondary = CustomPrimary.copy(alpha = 0.7f),
    background = CustomBackground,
    surface = CustomSurface,
    onSurface = CustomOnSurface,
    error = CustomError
)

@Composable
fun FitnessTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

