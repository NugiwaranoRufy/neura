package com.app.neura.data.local

import android.content.Context
import com.app.neura.data.model.AchievementProgress
import com.app.neura.data.model.GameSessionResult
import kotlinx.serialization.json.Json
import java.io.File

class AchievementProgressDataSource(
    private val context: Context
) {
    private val fileName = "achievement_progress.json"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getProgress(): AchievementProgress {
        return try {
            val file = getFile()
            if (!file.exists()) return AchievementProgress()

            val content = SafeFileStore.readTextOrNull(file) ?: return AchievementProgress()

            json.decodeFromString(
                AchievementProgress.serializer(),
                content
            )
        } catch (_: Exception) {
            AchievementProgress()
        }
    }

    fun saveProgress(progress: AchievementProgress) {
        SafeFileStore.writeTextWithBackup(
            file = getFile(),
            content = json.encodeToString(
                AchievementProgress.serializer(),
                progress
            )
        )
    }

    fun recordSession(
        result: GameSessionResult,
        currentDailyStreak: Int
    ) {
        val current = getProgress()

        val updated = current.copy(
            lifetimeSessions = current.lifetimeSessions + 1,
            lifetimePerfectScores = if (
                result.totalQuestions > 0 &&
                result.score == result.totalQuestions
            ) {
                current.lifetimePerfectScores + 1
            } else {
                current.lifetimePerfectScores
            },
            lifetimeDailyCompletions = if (result.source == "Daily challenge") {
                current.lifetimeDailyCompletions + 1
            } else {
                current.lifetimeDailyCompletions
            },
            bestDailyStreak = maxOf(current.bestDailyStreak, currentDailyStreak)
        )

        saveProgress(updated)
    }
}