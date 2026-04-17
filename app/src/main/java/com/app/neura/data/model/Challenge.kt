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
    val isUserCreated: Boolean = false
)

@Serializable
enum class ChallengeType {
    LOGIC,
    LATERAL
}