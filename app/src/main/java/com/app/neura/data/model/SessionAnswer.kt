package com.app.neura.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionAnswer(
    val challenge: Challenge,
    val selectedOptionIndex: Int?,
    val isCorrect: Boolean
)