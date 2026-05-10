package com.app.neura.data.model

data class PostSessionCoachSummary(
    val title: String,
    val message: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
    val recommendedConfig: GameSessionConfig,
    val shouldReviewAnswersFirst: Boolean,
    val focusLabel: String,
    val scoreLabel: String
)
