package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.MissionBadge
import com.app.neura.data.model.MissionBadgeCategory
import com.app.neura.data.model.MissionBadgesSummary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale

fun List<GameSessionResult>.buildMissionBadgesSummary(
    weeklyGoalSessions: Int,
    dailyCompletedToday: Boolean,
    trainingLevel: Int
): MissionBadgesSummary {
    val safeWeeklyGoal = weeklyGoalSessions.coerceIn(1, 7)
    val sessionsThisWeek = filterCompletedThisWeek()
    val completedThisWeek = sessionsThisWeek.size

    val hasAnySession = isNotEmpty()
    val hasPerfectSession = any { it.totalQuestions > 0 && it.score == it.totalQuestions }
    val hasPerfectSessionThisWeek = sessionsThisWeek.any {
        it.totalQuestions > 0 && it.score == it.totalQuestions
    }

    val completedLogicThisWeek = sessionsThisWeek.any { it.type == ChallengeType.LOGIC }
    val completedLateralThisWeek = sessionsThisWeek.any { it.type == ChallengeType.LATERAL }

    val totalCorrectAnswers = sumOf { it.score }
    val bestAccuracy = maxOfOrNull { it.accuracyPercent() } ?: 0
    val totalSessions = size

    val badges = listOf(
        MissionBadge(
            id = "first_session",
            title = "First Session",
            description = "Complete your first Neura training session.",
            icon = "🧠",
            isUnlocked = hasAnySession,
            unlockHint = "Complete one session to unlock this badge.",
            category = MissionBadgeCategory.STARTER
        ),
        MissionBadge(
            id = "daily_rhythm",
            title = "Daily Rhythm",
            description = "Complete today’s Daily Challenge.",
            icon = "☀️",
            isUnlocked = dailyCompletedToday,
            unlockHint = "Complete today’s Daily Challenge.",
            category = MissionBadgeCategory.DAILY
        ),
        MissionBadge(
            id = "weekly_finisher",
            title = "Weekly Finisher",
            description = "Reach your weekly training goal.",
            icon = "🎯",
            isUnlocked = completedThisWeek >= safeWeeklyGoal,
            unlockHint = "Complete $safeWeeklyGoal sessions this week.",
            category = MissionBadgeCategory.WEEKLY
        ),
        MissionBadge(
            id = "perfect_focus",
            title = "Perfect Focus",
            description = "Complete a perfect session.",
            icon = "🏆",
            isUnlocked = hasPerfectSession,
            unlockHint = "Score full marks in one session.",
            category = MissionBadgeCategory.PERFORMANCE
        ),
        MissionBadge(
            id = "weekly_perfect",
            title = "Weekly Perfect",
            description = "Complete a perfect session this week.",
            icon = "✨",
            isUnlocked = hasPerfectSessionThisWeek,
            unlockHint = "Complete one perfect session during the current week.",
            category = MissionBadgeCategory.PERFORMANCE
        ),
        MissionBadge(
            id = "balanced_mind",
            title = "Balanced Mind",
            description = "Complete both Logic and Lateral sessions this week.",
            icon = "⚖️",
            isUnlocked = completedLogicThisWeek && completedLateralThisWeek,
            unlockHint = "Complete at least one Logic and one Lateral session this week.",
            category = MissionBadgeCategory.BALANCE
        ),
        MissionBadge(
            id = "sharp_identity",
            title = "Sharp Identity",
            description = "Reach Training Identity level 5.",
            icon = "🪪",
            isUnlocked = trainingLevel >= 5,
            unlockHint = "Reach level 5 in your Training Identity.",
            category = MissionBadgeCategory.IDENTITY
        ),
        MissionBadge(
            id = "record_breaker",
            title = "Record Breaker",
            description = "Reach at least 90% accuracy in a session.",
            icon = "🥇",
            isUnlocked = bestAccuracy >= 90,
            unlockHint = "Reach 90% accuracy in a single session.",
            category = MissionBadgeCategory.RECORDS
        ),
        MissionBadge(
            id = "answer_builder",
            title = "Answer Builder",
            description = "Collect 50 correct answers.",
            icon = "✅",
            isUnlocked = totalCorrectAnswers >= 50,
            unlockHint = "Reach 50 total correct answers.",
            category = MissionBadgeCategory.RECORDS
        ),
        MissionBadge(
            id = "training_regular",
            title = "Training Regular",
            description = "Complete 10 total sessions.",
            icon = "🔥",
            isUnlocked = totalSessions >= 10,
            unlockHint = "Complete 10 sessions.",
            category = MissionBadgeCategory.WEEKLY
        )
    )

    val unlockedCount = badges.count { it.isUnlocked }
    val totalCount = badges.size

    val title = when {
        unlockedCount == totalCount -> "All badges unlocked"
        unlockedCount == 0 -> "Start unlocking badges"
        else -> "Mission badges"
    }

    val message = when {
        unlockedCount == totalCount -> {
            "Excellent work. You unlocked every current badge."
        }

        unlockedCount == 0 -> {
            "Complete your first training session to unlock your first badge."
        }

        else -> {
            "You unlocked $unlockedCount of $totalCount badges."
        }
    }

    return MissionBadgesSummary(
        title = title,
        message = message,
        unlockedCount = unlockedCount,
        totalCount = totalCount,
        progressText = "$unlockedCount of $totalCount unlocked",
        badges = badges
    )
}

private fun GameSessionResult.accuracyPercent(): Int {
    if (totalQuestions <= 0) return 0

    return ((score.toDouble() / totalQuestions.toDouble()) * 100)
        .toInt()
        .coerceIn(0, 100)
}

private fun List<GameSessionResult>.filterCompletedThisWeek(): List<GameSessionResult> {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val weekFields = WeekFields.of(Locale.getDefault())

    val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
    val currentYear = today.get(weekFields.weekBasedYear())

    return filter { result ->
        val completedDate = Instant
            .ofEpochMilli(result.completedAt)
            .atZone(zoneId)
            .toLocalDate()

        val resultWeek = completedDate.get(weekFields.weekOfWeekBasedYear())
        val resultYear = completedDate.get(weekFields.weekBasedYear())

        resultWeek == currentWeek && resultYear == currentYear
    }
}