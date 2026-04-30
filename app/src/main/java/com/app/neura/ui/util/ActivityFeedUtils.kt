package com.app.neura.ui.util

import com.app.neura.data.model.ActivityFeedCategory
import com.app.neura.data.model.ActivityFeedItem
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun List<GameSessionResult>.buildActivityFeed(
    limit: Int = 20
): List<ActivityFeedItem> {
    return sortedByDescending { it.completedAt }
        .take(limit)
        .map { result ->
            result.toActivityFeedItem()
        }
}

private fun GameSessionResult.toActivityFeedItem(): ActivityFeedItem {
    val isDaily = source.equals("Daily challenge", ignoreCase = true)
    val accuracy = if (totalQuestions == 0) {
        0
    } else {
        ((score.toDouble() / totalQuestions.toDouble()) * 100).toInt()
    }

    val category = when {
        isDaily -> ActivityFeedCategory.DAILY
        accuracy == 100 -> ActivityFeedCategory.MILESTONE
        else -> ActivityFeedCategory.SESSION
    }

    val title = when {
        isDaily -> "Daily Challenge completed"
        accuracy == 100 -> "Perfect session completed"
        type == ChallengeType.LOGIC -> "Logic session completed"
        type == ChallengeType.LATERAL -> "Lateral thinking session completed"
        else -> "Training session completed"
    }

    val icon = when (category) {
        ActivityFeedCategory.DAILY -> "☀️"
        ActivityFeedCategory.MILESTONE -> "🏆"
        ActivityFeedCategory.SESSION -> "🧠"
    }

    val typeText = when (type) {
        ChallengeType.LOGIC -> "Logic"
        ChallengeType.LATERAL -> "Lateral"
        null -> "Mixed"
    }

    val dateText = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(Date(completedAt))

    return ActivityFeedItem(
        id = "session_$id",
        title = title,
        message = "Score $score/$totalQuestions • $accuracy%",
        metadata = "$typeText • $source • $dateText",
        icon = icon,
        timestamp = completedAt,
        category = category
    )
}