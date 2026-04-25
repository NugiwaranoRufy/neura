package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.GameSessionResult
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

class SessionHistoryLocalDataSource(
    private val context: Context
) {
    private val fileName = "session_history.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getSessions(): List<GameSessionResult> {
        return try {
            val file = getFile()
            if (!file.exists()) return emptyList()

            val content = file.readText()
            if (content.isBlank()) return emptyList()

            json.decodeFromString(
                ListSerializer(GameSessionResult.serializer()),
                content
            )
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveSessions(sessions: List<GameSessionResult>) {
        getFile().writeText(
            json.encodeToString(
                ListSerializer(GameSessionResult.serializer()),
                sessions
            )
        )
    }

    fun addSession(session: GameSessionResult) {
        val updated = getSessions()
            .plus(session)
            .sortedByDescending { it.completedAt }

        saveSessions(updated)
    }

    fun clearSessions() {
        saveSessions(emptyList())
    }
}