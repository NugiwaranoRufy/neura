package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.OngoingSession
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OngoingSessionDataSource(
    context: Context
) {
    private val prefs = context.getSharedPreferences(
        "ongoing_session_prefs",
        Context.MODE_PRIVATE
    )

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun saveSession(session: OngoingSession) {
        val encoded = json.encodeToString(session)

        prefs.edit()
            .putString(KEY_SESSION, encoded)
            .apply()
    }

    fun getSession(): OngoingSession? {
        val encoded = prefs.getString(KEY_SESSION, null) ?: return null

        return try {
            json.decodeFromString<OngoingSession>(encoded)
        } catch (_: Exception) {
            null
        }
    }

    fun hasSession(): Boolean {
        return prefs.contains(KEY_SESSION)
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_SESSION)
            .apply()
    }

    private companion object {
        const val KEY_SESSION = "ongoing_session"
    }
}