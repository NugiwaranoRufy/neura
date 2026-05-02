package com.app.neura.data.model

data class BadgeUnlockSummary(
    val title: String,
    val message: String,
    val unlockedBadges: List<MissionBadge>
)