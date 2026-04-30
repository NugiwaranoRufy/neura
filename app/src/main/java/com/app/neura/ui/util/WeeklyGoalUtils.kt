package com.app.neura.ui.util

import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.WeeklyGoalProgress
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale

fun List<GameSessionResult>.buildWeeklyGoalProgress(
    goalSessions: Int
): WeeklyGoalProgress {
    val safeGoal = goalSessions.coerceIn(1, 7)
    val completedThisWeek = countCompletedThisWeek()
    val progressPercent = ((completedThisWeek.toDouble() / safeGoal.toDouble()) * 100)
        .toInt()
        .coerceIn(0, 100)

    val isCompleted = completedThisWeek >= safeGoal

    val title = if (isCompleted) {
        "Weekly goal completed"
    } else {
        "Weekly goal"
    }

    val message = when {
        isCompleted -> {
            "Great work. You reached your weekly training goal."
        }

        completedThisWeek == 0 -> {
            "Start with a short session to build momentum this week."
        }

        completedThisWeek == safeGoal - 1 -> {
            "You are one session away from your weekly goal."
        }

        else -> {
            "Keep going. You are building a consistent training habit."
        }
    }

    val actionLabel = if (isCompleted) {
        "Train more"
    } else {
        "Start training"
    }

    return WeeklyGoalProgress(
        completedSessions = completedThisWeek,
        goalSessions = safeGoal,
        progressPercent = progressPercent,
        isCompleted = isCompleted,
        title = title,
        message = message,
        actionLabel = actionLabel
    )
}

private fun List<GameSessionResult>.countCompletedThisWeek(): Int {
    val zoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zoneId)
    val currentWeek = today.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())
    val currentYear = today.get(WeekFields.of(Locale.getDefault()).weekBasedYear())

    return count { result ->
        val completedDate = Instant
            .ofEpochMilli(result.completedAt)
            .atZone(zoneId)
            .toLocalDate()

        val resultWeek = completedDate.get(
            WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
        )

        val resultYear = completedDate.get(
            WeekFields.of(Locale.getDefault()).weekBasedYear()
        )

        resultWeek == currentWeek && resultYear == currentYear
    }
}