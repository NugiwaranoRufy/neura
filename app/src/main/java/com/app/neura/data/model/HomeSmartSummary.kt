package com.app.neura.data.model

data class HomeSmartSummary(
    val title: String,
    val message: String,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
    val recommendedConfig: GameSessionConfig,
    val shouldResumeSession: Boolean,
    val focusLabel: String,
    val progressLabel: String
)