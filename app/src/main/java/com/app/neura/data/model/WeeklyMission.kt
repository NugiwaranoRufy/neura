package com.app.neura.data.model

data class WeeklyMissionsSummary(
    val title: String,
    val message: String,
    val completedCount: Int,
    val totalCount: Int,
    val progressText: String,
    val missions: List<WeeklyMission>
)

data class WeeklyMission(
    val id: String,
    val title: String,
    val description: String,
    val progressText: String,
    val icon: String,
    val isCompleted: Boolean,
    val recommendedType: ChallengeType,
    val recommendedDifficulty: ChallengeDifficulty?,
    val recommendedQuestionCount: Int
)