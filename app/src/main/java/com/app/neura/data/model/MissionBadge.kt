package com.app.neura.data.model

data class MissionBadgesSummary(
    val title: String,
    val message: String,
    val unlockedCount: Int,
    val totalCount: Int,
    val progressText: String,
    val badges: List<MissionBadge>
)

data class MissionBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val unlockHint: String,
    val category: MissionBadgeCategory
)

enum class MissionBadgeCategory {
    STARTER,
    DAILY,
    WEEKLY,
    PERFORMANCE,
    BALANCE,
    IDENTITY,
    RECORDS
}