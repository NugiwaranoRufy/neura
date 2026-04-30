package com.app.neura.data.model

data class TrainingIdentity(
    val title: String,
    val level: Int,
    val totalSessions: Int,
    val averageAccuracyText: String,
    val mainStyleText: String,
    val bestAreaText: String,
    val completedThisWeek: Int,
    val weeklyGoalSessions: Int,
    val weeklyProgressText: String,
    val message: String
)