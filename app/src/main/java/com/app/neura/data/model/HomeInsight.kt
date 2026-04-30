package com.app.neura.data.model

data class HomeInsight(
    val title: String,
    val message: String,
    val totalSessions: Int,
    val averageAccuracyText: String,
    val currentDailyStreak: Int,
    val suggestedType: ChallengeType,
    val suggestedDifficulty: ChallengeDifficulty?,
    val suggestedQuestionCount: Int,
    val primaryActionLabel: String,
    val shouldResumeSession: Boolean
)