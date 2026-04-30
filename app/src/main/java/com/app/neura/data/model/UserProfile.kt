package com.app.neura.data.model

data class UserProfile(
    val displayName: String = "You",
    val username: String = "neura_user",
    val bio: String = "",
    val favoriteTag: String = "",
    val weeklyGoalSessions: Int = 3
)