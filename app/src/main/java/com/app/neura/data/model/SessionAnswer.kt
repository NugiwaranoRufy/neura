package com.app.neura.data.model

data class SessionAnswer(
    val challenge: Challenge,
    val selectedOptionIndex: Int?,
    val isCorrect: Boolean
)