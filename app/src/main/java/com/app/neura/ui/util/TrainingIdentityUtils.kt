package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.TrainingIdentity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.max

fun List<GameSessionResult>.buildTrainingIdentity(
    weeklyGoalSessions: Int
): TrainingIdentity {
    val safeWeeklyGoal = weeklyGoalSessions.coerceIn(1, 7)
    val totalSessions = size
    val averageAccuracy = averageAccuracyPercent()
    val completedThisWeek = countCompletedThisWeek()
    val level = calculateTrainingLevel(
        totalSessions = totalSessions,
        averageAccuracy = averageAccuracy
    )

    val title = buildTrainingTitle(
        totalSessions = totalSessions,
        averageAccuracy = averageAccuracy,
        completedThisWeek = completedThisWeek,
        weeklyGoalSessions = safeWeeklyGoal
    )

    val message = buildTrainingMessage(
        totalSessions = totalSessions,
        averageAccuracy = averageAccuracy,
        completedThisWeek = completedThisWeek,
        weeklyGoalSessions = safeWeeklyGoal
    )

    return TrainingIdentity(
        title = title,
        level = level,
        totalSessions = totalSessions,
        averageAccuracyText = if (totalSessions == 0) "N/A" else "$averageAccuracy%",
        mainStyleText = mostTrainedTypeText(),
        bestAreaText = bestAreaTypeText(),
        completedThisWeek = completedThisWeek,
        weeklyGoalSessions = safeWeeklyGoal,
        weeklyProgressText = "$completedThisWeek of $safeWeeklyGoal sessions this week",
        message = message
    )
}

private fun List<GameSessionResult>.averageAccuracyPercent(): Int {
    if (isEmpty()) return 0

    val average = map { result ->
        if (result.totalQuestions == 0) {
            0.0
        } else {
            result.score.toDouble() / result.totalQuestions.toDouble()
        }
    }.average()

    return (average * 100).toInt().coerceIn(0, 100)
}

private fun calculateTrainingLevel(
    totalSessions: Int,
    averageAccuracy: Int
): Int {
    if (totalSessions <= 0) return 1

    val sessionPoints = totalSessions * 10
    val accuracyBonus = averageAccuracy / 5
    val rawLevel = ((sessionPoints + accuracyBonus) / 50) + 1

    return rawLevel.coerceIn(1, 99)
}

private fun buildTrainingTitle(
    totalSessions: Int,
    averageAccuracy: Int,
    completedThisWeek: Int,
    weeklyGoalSessions: Int
): String {
    return when {
        totalSessions == 0 -> "New Thinker"
        completedThisWeek >= weeklyGoalSessions -> "Consistency Builder"
        averageAccuracy >= 90 && totalSessions >= 5 -> "Sharp Solver"
        averageAccuracy >= 75 && totalSessions >= 3 -> "Mind Builder"
        totalSessions >= 10 -> "Training Regular"
        else -> "Rising Thinker"
    }
}

private fun buildTrainingMessage(
    totalSessions: Int,
    averageAccuracy: Int,
    completedThisWeek: Int,
    weeklyGoalSessions: Int
): String {
    return when {
        totalSessions == 0 -> {
            "Complete your first session to start shaping your Training Identity."
        }

        completedThisWeek >= weeklyGoalSessions -> {
            "You reached your weekly rhythm. Keep going to strengthen your profile."
        }

        averageAccuracy >= 85 -> {
            "Your accuracy is strong. Try longer or harder sessions to keep growing."
        }

        completedThisWeek == 0 -> {
            "Start with a short session this week to rebuild your training rhythm."
        }

        else -> {
            "You are building a consistent training habit."
        }
    }
}

private fun List<GameSessionResult>.mostTrainedTypeText(): String {
    val type = filter { it.type != null }
        .groupBy { it.type }
        .maxByOrNull { it.value.size }
        ?.key

    return type.toReadableText(default = "Mixed")
}

private fun List<GameSessionResult>.bestAreaTypeText(): String {
    val bestType = filter { it.type != null && it.totalQuestions > 0 }
        .groupBy { it.type }
        .mapNotNull { entry ->
            val type = entry.key ?: return@mapNotNull null

            val average = entry.value.map { result ->
                result.score.toDouble() / max(result.totalQuestions, 1).toDouble()
            }.average()

            type to average
        }
        .maxByOrNull { it.second }
        ?.first

    return bestType.toReadableText(default = "Not enough data")
}

private fun ChallengeType?.toReadableText(default: String): String {
    return when (this) {
        ChallengeType.LOGIC -> "Logic"
        ChallengeType.LATERAL -> "Lateral"
        null -> default
    }
}

private fun List<GameSessionResult>.countCompletedThisWeek(): Int {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val weekFields = WeekFields.of(Locale.getDefault())

    val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
    val currentYear = today.get(weekFields.weekBasedYear())

    return count { result ->
        val completedDate = Instant
            .ofEpochMilli(result.completedAt)
            .atZone(zoneId)
            .toLocalDate()

        val resultWeek = completedDate.get(weekFields.weekOfWeekBasedYear())
        val resultYear = completedDate.get(weekFields.weekBasedYear())

        resultWeek == currentWeek && resultYear == currentYear
    }
}