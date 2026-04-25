package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.AccessibilitySettings
import kotlinx.serialization.json.Json
import java.io.File

class AccessibilitySettingsDataSource(
    private val context: Context
) {
    private val fileName = "accessibility_settings.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getSettings(): AccessibilitySettings {
        return try {
            val file = getFile()
            if (!file.exists()) return AccessibilitySettings()

            val content = file.readText()
            if (content.isBlank()) return AccessibilitySettings()

            json.decodeFromString(
                AccessibilitySettings.serializer(),
                content
            )
        } catch (_: Exception) {
            AccessibilitySettings()
        }
    }

    fun saveSettings(settings: AccessibilitySettings) {
        getFile().writeText(
            json.encodeToString(
                AccessibilitySettings.serializer(),
                settings
            )
        )
    }
}