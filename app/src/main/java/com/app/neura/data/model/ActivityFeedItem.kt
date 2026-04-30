package com.app.neura.data.model

data class ActivityFeedItem(
    val id: String,
    val title: String,
    val message: String,
    val metadata: String,
    val icon: String,
    val timestamp: Long,
    val category: ActivityFeedCategory
)

enum class ActivityFeedCategory {
    SESSION,
    DAILY,
    MILESTONE
}