package com.app.neura.data.model

data class Challenge(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val type: ChallengeType
)

enum class ChallengeType {
    LOGIC,
    LATERAL
}