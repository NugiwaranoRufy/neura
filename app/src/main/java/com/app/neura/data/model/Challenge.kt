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
    val updatedAt: Long = System.currentTimeMillis(),
    val authorName: String = "You",
    val tags: List<String> = emptyList(),
    val editorialStatus: EditorialStatus = EditorialStatus.DRAFT,
    val visibilityStatus: VisibilityStatus = VisibilityStatus.PRIVATE,
    val publishedAt: Long? = null
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