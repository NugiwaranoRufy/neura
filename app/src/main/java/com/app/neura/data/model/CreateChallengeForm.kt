package com.app.neura.data.model

data class CreateChallengeForm(
    val question: String = "",
    val option1: String = "",
    val option2: String = "",
    val option3: String = "",
    val option4: String = "",
    val correctIndex: Int = 0,
    val explanation: String = "",
    val type: ChallengeType = ChallengeType.LOGIC,
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    val authorName: String = "You",
    val tags: List<String> = emptyList()
)