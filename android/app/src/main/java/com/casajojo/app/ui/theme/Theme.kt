package com.casajojo.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores para el tema oscuro de La Casa De JoJo
private val DarkColorScheme = darkColorScheme(
    primary = YellowLogo,            // Color primario de acción (botones amarillos)
    onPrimary = DarkBackground,      // Texto sobre botones primarios
    secondary = OrangePrimary,       // Naranjo de la marca para acentos
    onSecondary = TextWhite,
    background = DarkBackground,     // Fondo general oscuro
    onBackground = TextWhite,
    surface = DarkSurface,           // Tarjetas y modales
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    error = ErrorRed
)

@Composable
fun CasaJoJoTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Adapta la barra de estado superior al fondo oscuro de la app
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de tener tu Typography.kt estándar
        content = content
    )
}