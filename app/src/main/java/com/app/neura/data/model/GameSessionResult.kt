package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSessionResult(
    val id: Long = System.currentTimeMillis(),
    val type: ChallengeType? = null,
    val score: Int,
    val totalQuestions: Int,
    val completedAt: Long = System.currentTimeMillis()
)