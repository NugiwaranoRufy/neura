package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Serializable
enum class TextScale {
    SMALL,
    NORMAL,
    LARGE,
    EXTRA_LARGE
}

@Serializable
enum class ColorVisionMode {
    DEFAULT,
    PROTANOPIA,
    DEUTERANOPIA,
    TRITANOPIA
}

@Serializable
data class AccessibilitySettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val textScale: TextScale = TextScale.NORMAL,
    val highContrast: Boolean = false,
    val colorVisionMode: ColorVisionMode = ColorVisionMode.DEFAULT,
    val calmMode: Boolean = false,
    val focusMode: Boolean = false,
    val readingHelper: Boolean = false,
    val confirmDestructiveActions: Boolean = true,
    val reduceMotion: Boolean = false
)