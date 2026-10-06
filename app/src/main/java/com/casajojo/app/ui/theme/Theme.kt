package com.casajojo.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.Black,
    secondary = OrangeSecondary,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    error = ErrorRed
)

@Composable
fun CasaJoJoAppTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // En false para que Android no cambie tus colores por el fondo de pantalla
    content: @Composable () -> Unit
) {
    // Usamos directamente nuestro esquema de colores oscuros de la marca
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}