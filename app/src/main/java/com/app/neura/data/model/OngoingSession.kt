package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OngoingSession(
    val challenges: List<Challenge>,
    val currentIndex: Int,
    val score: Int,
    val answers: List<SessionAnswer>,
    val source: String,
    val sessionType: ChallengeType?
)