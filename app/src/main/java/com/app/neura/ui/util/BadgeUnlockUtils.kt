package com.app.neura.ui.util

import com.app.neura.data.model.BadgeUnlockSummary
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.MissionBadge
import com.app.neura.data.model.MissionBadgeCategory
import com.app.neura.data.model.MissionBadgesSummary

fun buildBadgeUnlockSummary(
    latestResult: GameSessionResult?,
    badgesSummary: MissionBadgesSummary
): BadgeUnlockSummary {
    if (latestResult == null) {
        return BadgeUnlockSummary(
            title = "Keep going",
            message = "Complete a session to unlock badges and build your profile.",
            unlockedBadges = emptyList()
        )
    }

    val candidates = badgesSummary.badges
        .filter { it.isUnlocked }
        .filter { badge ->
            badge.isRelevantForLatestResult(latestResult)
        }
        .distinctBy { it.id }

    if (candidates.isEmpty()) {
        return BadgeUnlockSummary(
            title = "Keep going",
            message = "Complete missions and improve your records to unlock more badges.",
            unlockedBadges = emptyList()
        )
    }

    val title = if (candidates.size == 1) {
        "Badge unlocked"
    } else {
        "Badges unlocked"
    }

    val message = if (candidates.size == 1) {
        "You unlocked a new milestone from this session."
    } else {
        "You unlocked ${candidates.size} milestones from this session."
    }

    return BadgeUnlockSummary(
        title = title,
        message = message,
        unlockedBadges = candidates
    )
}

private fun MissionBadge.isRelevantForLatestResult(
    latestResult: GameSessionResult
): Boolean {
    val isPerfect = latestResult.totalQuestions > 0 &&
            latestResult.score == latestResult.totalQuestions

    val accuracy = latestResult.accuracyPercent()

    return when (id) {
        "first_session" -> true

        "perfect_focus" -> isPerfect

        "weekly_perfect" -> isPerfect

        "record_breaker" -> accuracy >= 90

        "answer_builder" -> true

        "training_regular" -> true

        "sharp_identity" -> true

        "balanced_mind" -> {
            latestResult.type == ChallengeType.LOGIC ||
                    latestResult.type == ChallengeType.LATERAL
        }

        "weekly_finisher" -> true

        "daily_rhythm" -> {
            latestResult.source.equals("Daily challenge", ignoreCase = true)
        }

        else -> {
            category == MissionBadgeCategory.PERFORMANCE ||
                    category == MissionBadgeCategory.RECORDS
        }
    }
}

private fun GameSessionResult.accuracyPercent(): Int {
    if (totalQuestions <= 0) return 0

    return ((score.toDouble() / totalQuestions.toDouble()) * 100)
        .toInt()
        .coerceIn(0, 100)
}