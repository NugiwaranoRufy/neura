package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AchievementProgress(
    val lifetimeSessions: Int = 0,
    val lifetimePerfectScores: Int = 0,
    val lifetimeDailyCompletions: Int = 0,
    val bestDailyStreak: Int = 0
)