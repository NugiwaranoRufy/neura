package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Challenge(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val type: ChallengeType,
    val isUserCreated: Boolean = false,
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    val createdAt: Long = System.currentTimeMillis(),
    val authorName: String = "You"
)

@Serializable
enum class ChallengeType {
    LOGIC,
    LATERAL
}

@Serializable
enum class ChallengeDifficulty {
    EASY,
    MEDIUM,
    HARD
}