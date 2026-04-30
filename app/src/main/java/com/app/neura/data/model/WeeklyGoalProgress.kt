package com.app.neura.data.model

data class WeeklyGoalProgress(
    val completedSessions: Int,
    val goalSessions: Int,
    val progressPercent: Int,
    val isCompleted: Boolean,
    val title: String,
    val message: String,
    val actionLabel: String
)