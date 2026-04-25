package com.app.neura.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.app.neura.data.model.ColorVisionMode
import com.app.neura.data.model.TextScale
import com.app.neura.data.model.ThemeMode

private val DefaultDarkColorScheme = darkColorScheme()
private val DefaultLightColorScheme = lightColorScheme()

private val HighContrastLightColorScheme = lightColorScheme(
    primary = Color(0xFF000000),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF000000),
    onPrimaryContainer = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFECECEC),
    onSurfaceVariant = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1F1F1F),
    onSurfaceVariant = Color.White,
    error = Color(0xFFFFB4AB),
    onError = Color.Black
)

private fun colorVisionScheme(
    darkTheme: Boolean,
    mode: ColorVisionMode
): ColorScheme {
    return when (mode) {
        ColorVisionMode.DEFAULT -> {
            if (darkTheme) DefaultDarkColorScheme else DefaultLightColorScheme
        }

        ColorVisionMode.PROTANOPIA -> {
            if (darkTheme) {
                darkColorScheme(
                    primary = Color(0xFF7DD3FC),
                    primaryContainer = Color(0xFF0C4A6E),
                    secondary = Color(0xFFFDE68A),
                    surfaceVariant = Color(0xFF1E293B)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF0369A1),
                    primaryContainer = Color(0xFFBAE6FD),
                    secondary = Color(0xFF92400E),
                    surfaceVariant = Color(0xFFE0F2FE)
                )
            }
        }

        ColorVisionMode.DEUTERANOPIA -> {
            if (darkTheme) {
                darkColorScheme(
                    primary = Color(0xFF93C5FD),
                    primaryContainer = Color(0xFF1E3A8A),
                    secondary = Color(0xFFFCD34D),
                    surfaceVariant = Color(0xFF1E293B)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF1D4ED8),
                    primaryContainer = Color(0xFFDBEAFE),
                    secondary = Color(0xFFB45309),
                    surfaceVariant = Color(0xFFEFF6FF)
                )
            }
        }

        ColorVisionMode.TRITANOPIA -> {
            if (darkTheme) {
                darkColorScheme(
                    primary = Color(0xFFF9A8D4),
                    primaryContainer = Color(0xFF831843),
                    secondary = Color(0xFFA7F3D0),
                    surfaceVariant = Color(0xFF292524)
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFFBE185D),
                    primaryContainer = Color(0xFFFCE7F3),
                    secondary = Color(0xFF047857),
                    surfaceVariant = Color(0xFFFFF1F2)
                )
            }
        }
    }
}

private fun scaledTypography(scale: TextScale): Typography {
    val multiplier = when (scale) {
        TextScale.SMALL -> 0.9f
        TextScale.NORMAL -> 1.0f
        TextScale.LARGE -> 1.15f
        TextScale.EXTRA_LARGE -> 1.3f
    }

    fun TextUnit.scaled(): TextUnit {
        return (this.value * multiplier).sp
    }

    val base = Typography()

    return Typography(
        displayLarge = base.displayLarge.copy(fontSize = base.displayLarge.fontSize.scaled()),
        displayMedium = base.displayMedium.copy(fontSize = base.displayMedium.fontSize.scaled()),
        displaySmall = base.displaySmall.copy(fontSize = base.displaySmall.fontSize.scaled()),
        headlineLarge = base.headlineLarge.copy(fontSize = base.headlineLarge.fontSize.scaled()),
        headlineMedium = base.headlineMedium.copy(fontSize = base.headlineMedium.fontSize.scaled()),
        headlineSmall = base.headlineSmall.copy(fontSize = base.headlineSmall.fontSize.scaled()),
        titleLarge = base.titleLarge.copy(fontSize = base.titleLarge.fontSize.scaled()),
        titleMedium = base.titleMedium.copy(fontSize = base.titleMedium.fontSize.scaled()),
        titleSmall = base.titleSmall.copy(fontSize = base.titleSmall.fontSize.scaled()),
        bodyLarge = base.bodyLarge.copy(fontSize = base.bodyLarge.fontSize.scaled()),
        bodyMedium = base.bodyMedium.copy(fontSize = base.bodyMedium.fontSize.scaled()),
        bodySmall = base.bodySmall.copy(fontSize = base.bodySmall.fontSize.scaled()),
        labelLarge = base.labelLarge.copy(fontSize = base.labelLarge.fontSize.scaled()),
        labelMedium = base.labelMedium.copy(fontSize = base.labelMedium.fontSize.scaled()),
        labelSmall = base.labelSmall.copy(fontSize = base.labelSmall.fontSize.scaled())
    )
}

@Composable
fun NeuraTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    textScale: TextScale = TextScale.NORMAL,
    highContrast: Boolean = false,
    colorVisionMode: ColorVisionMode = ColorVisionMode.DEFAULT,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        highContrast && darkTheme -> HighContrastDarkColorScheme
        highContrast && !darkTheme -> HighContrastLightColorScheme
        else -> colorVisionScheme(
            darkTheme = darkTheme,
            mode = colorVisionMode
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography(textScale),
        content = content
    )
}