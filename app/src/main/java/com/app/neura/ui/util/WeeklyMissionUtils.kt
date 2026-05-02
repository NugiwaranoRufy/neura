package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.WeeklyMission
import com.app.neura.data.model.WeeklyMissionsSummary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale

fun List<GameSessionResult>.buildWeeklyMissionsSummary(
    weeklyGoalSessions: Int,
    dailyCompletedToday: Boolean
): WeeklyMissionsSummary {
    val safeWeeklyGoal = weeklyGoalSessions.coerceIn(1, 7)
    val sessionsThisWeek = filterCompletedThisWeek()
    val completedThisWeek = sessionsThisWeek.size
    val hasPerfectSessionThisWeek = sessionsThisWeek.any {
        it.totalQuestions > 0 && it.score == it.totalQuestions
    }

    val completedLogicThisWeek = sessionsThisWeek.any {
        it.type == ChallengeType.LOGIC
    }

    val completedLateralThisWeek = sessionsThisWeek.any {
        it.type == ChallengeType.LATERAL
    }

    val averageAccuracy = averageAccuracyPercent()
    val focusDifficulty = when {
        averageAccuracy < 55 -> ChallengeDifficulty.EASY
        averageAccuracy < 80 -> ChallengeDifficulty.MEDIUM
        else -> ChallengeDifficulty.HARD
    }

    val missions = listOf(
        WeeklyMission(
            id = "daily_rhythm",
            title = "Daily rhythm",
            description = "Complete today’s Daily Challenge or a short training session.",
            progressText = if (dailyCompletedToday) {
                "Completed today"
            } else {
                "Not completed today"
            },
            icon = "☀️",
            isCompleted = dailyCompletedToday,
            recommendedType = ChallengeType.LOGIC,
            recommendedDifficulty = ChallengeDifficulty.EASY,
            recommendedQuestionCount = 3
        ),
        WeeklyMission(
            id = "weekly_goal",
            title = "Weekly goal",
            description = "Complete your selected weekly training goal.",
            progressText = "$completedThisWeek of $safeWeeklyGoal sessions",
            icon = "🎯",
            isCompleted = completedThisWeek >= safeWeeklyGoal,
            recommendedType = weakestChallengeType() ?: ChallengeType.LOGIC,
            recommendedDifficulty = focusDifficulty,
            recommendedQuestionCount = 3
        ),
        WeeklyMission(
            id = "perfect_focus",
            title = "Perfect focus",
            description = "Complete one perfect session this week.",
            progressText = if (hasPerfectSessionThisWeek) {
                "Perfect session completed"
            } else {
                "No perfect session yet"
            },
            icon = "🏆",
            isCompleted = hasPerfectSessionThisWeek,
            recommendedType = strongestChallengeType() ?: ChallengeType.LOGIC,
            recommendedDifficulty = ChallengeDifficulty.EASY,
            recommendedQuestionCount = 3
        ),
        WeeklyMission(
            id = "balanced_training",
            title = "Balanced training",
            description = "Complete both Logic and Lateral sessions this week.",
            progressText = buildBalancedProgressText(
                completedLogic = completedLogicThisWeek,
                completedLateral = completedLateralThisWeek
            ),
            icon = "⚖️",
            isCompleted = completedLogicThisWeek && completedLateralThisWeek,
            recommendedType = when {
                !completedLogicThisWeek -> ChallengeType.LOGIC
                !completedLateralThisWeek -> ChallengeType.LATERAL
                else -> weakestChallengeType() ?: ChallengeType.LOGIC
            },
            recommendedDifficulty = ChallengeDifficulty.MEDIUM,
            recommendedQuestionCount = 3
        )
    )

    val completedCount = missions.count { it.isCompleted }
    val totalCount = missions.size

    val title = when {
        completedCount == totalCount -> "Weekly missions completed"
        completedCount == 0 -> "Start your weekly missions"
        else -> "Weekly missions"
    }

    val message = when {
        completedCount == totalCount -> {
            "Great work. You completed all weekly missions."
        }

        completedCount == 0 -> {
            "Complete your first mission to build momentum this week."
        }

        else -> {
            "You completed $completedCount of $totalCount weekly missions."
        }
    }

    return WeeklyMissionsSummary(
        title = title,
        message = message,
        completedCount = completedCount,
        totalCount = totalCount,
        progressText = "$completedCount of $totalCount completed",
        missions = missions
    )
}

private fun List<GameSessionResult>.averageAccuracyPercent(): Int {
    if (isEmpty()) return 0

    val average = map { result ->
        if (result.totalQuestions <= 0) {
            0.0
        } else {
            result.score.toDouble() / result.totalQuestions.toDouble()
        }
    }.average()

    return (average * 100).toInt().coerceIn(0, 100)
}

private fun List<GameSessionResult>.weakestChallengeType(): ChallengeType? {
    return filter { it.type != null && it.totalQuestions > 0 }
        .groupBy { it.type }
        .mapNotNull { entry ->
            val type = entry.key ?: return@mapNotNull null

            val average = entry.value.map { result ->
                result.score.toDouble() / result.totalQuestions.toDouble()
            }.average()

            type to average
        }
        .minByOrNull { it.second }
        ?.first
}

private fun List<GameSessionResult>.strongestChallengeType(): ChallengeType? {
    return filter { it.type != null && it.totalQuestions > 0 }
        .groupBy { it.type }
        .mapNotNull { entry ->
            val type = entry.key ?: return@mapNotNull null

            val average = entry.value.map { result ->
                result.score.toDouble() / result.totalQuestions.toDouble()
            }.average()

            type to average
        }
        .maxByOrNull { it.second }
        ?.first
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

private fun buildBalancedProgressText(
    completedLogic: Boolean,
    completedLateral: Boolean
): String {
    return when {
        completedLogic && completedLateral -> "Logic and Lateral completed"
        completedLogic -> "Logic completed, Lateral missing"
        completedLateral -> "Lateral completed, Logic missing"
        else -> "Logic and Lateral missing"
    }
}