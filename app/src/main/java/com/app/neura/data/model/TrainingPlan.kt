package com.app.neura.data.model

data class TrainingPlanSummary(
    val title: String,
    val message: String,
    val focusAreaText: String,
    val recommendedType: ChallengeType,
    val recommendedDifficulty: ChallengeDifficulty?,
    val recommendedQuestionCount: Int,
    val primaryActionLabel: String,
    val steps: List<TrainingPlanStep>
)

data class TrainingPlanStep(
    val title: String,
    val description: String,
    val type: ChallengeType,
    val difficulty: ChallengeDifficulty?,
    val questionCount: Int,
    val actionLabel: String,
    val isPrimary: Boolean
)