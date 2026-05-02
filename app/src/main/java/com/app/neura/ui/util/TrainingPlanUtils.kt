package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.TrainingPlanStep
import com.app.neura.data.model.TrainingPlanSummary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.Locale

fun List<GameSessionResult>.buildTrainingPlanSummary(
    weeklyGoalSessions: Int,
    dailyCompletedToday: Boolean,
    hasOngoingSession: Boolean
): TrainingPlanSummary {
    val safeWeeklyGoal = weeklyGoalSessions.coerceIn(1, 7)
    val completedThisWeek = countCompletedThisWeek()
    val averageAccuracy = averageAccuracyPercent()
    val weakestType = weakestChallengeType() ?: ChallengeType.LOGIC
    val strongestType = strongestChallengeType() ?: oppositeOf(weakestType)

    val recommendedDifficulty = when {
        averageAccuracy == 0 -> ChallengeDifficulty.EASY
        averageAccuracy < 55 -> ChallengeDifficulty.EASY
        averageAccuracy < 80 -> ChallengeDifficulty.MEDIUM
        else -> ChallengeDifficulty.HARD
    }

    val recommendedQuestionCount = when {
        averageAccuracy < 60 -> 3
        completedThisWeek >= safeWeeklyGoal -> 5
        else -> 3
    }

    val title = when {
        hasOngoingSession -> "Resume your training plan"
        completedThisWeek >= safeWeeklyGoal -> "Stretch your weekly progress"
        !dailyCompletedToday -> "Complete today’s rhythm"
        isEmpty() -> "Start your first plan"
        else -> "Adaptive training plan"
    }

    val message = when {
        hasOngoingSession -> {
            "You have an open session. Resume it first, then continue with your weekly plan."
        }

        isEmpty() -> {
            "Start with a short Logic session so Neura can learn your training style."
        }

        completedThisWeek >= safeWeeklyGoal -> {
            "Your weekly goal is complete. Try a harder session to keep improving."
        }

        !dailyCompletedToday -> {
            "Start with your Daily Challenge, then train your focus area."
        }

        else -> {
            "Focus on your weakest area with a short targeted session."
        }
    }

    val focusAreaText = weakestType.toPlanText()

    val warmupStep = TrainingPlanStep(
        title = if (dailyCompletedToday) "Warm up" else "Daily rhythm",
        description = if (dailyCompletedToday) {
            "Start with a short session to get into flow."
        } else {
            "Complete your Daily Challenge to protect your streak."
        },
        type = weakestType,
        difficulty = ChallengeDifficulty.EASY,
        questionCount = 3,
        actionLabel = if (dailyCompletedToday) "Start warm up" else "Start daily-style session",
        isPrimary = false
    )

    val focusStep = TrainingPlanStep(
        title = "Focus area",
        description = "Train ${weakestType.toPlanText()} with the difficulty Neura recommends for you.",
        type = weakestType,
        difficulty = recommendedDifficulty,
        questionCount = recommendedQuestionCount,
        actionLabel = "Start focus session",
        isPrimary = true
    )

    val stretchStep = TrainingPlanStep(
        title = "Stretch goal",
        description = "Train your secondary area with a longer session.",
        type = strongestType,
        difficulty = if (averageAccuracy >= 75) {
            ChallengeDifficulty.HARD
        } else {
            ChallengeDifficulty.MEDIUM
        },
        questionCount = 5,
        actionLabel = "Start stretch session",
        isPrimary = false
    )

    return TrainingPlanSummary(
        title = title,
        message = message,
        focusAreaText = focusAreaText,
        recommendedType = weakestType,
        recommendedDifficulty = recommendedDifficulty,
        recommendedQuestionCount = recommendedQuestionCount,
        primaryActionLabel = if (hasOngoingSession) {
            "Resume first"
        } else {
            "Start recommended session"
        },
        steps = listOf(
            warmupStep,
            focusStep,
            stretchStep
        )
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

private fun ChallengeType.toPlanText(): String {
    return when (this) {
        ChallengeType.LOGIC -> "Logic"
        ChallengeType.LATERAL -> "Lateral thinking"
    }
}

private fun oppositeOf(type: ChallengeType): ChallengeType {
    return when (type) {
        ChallengeType.LOGIC -> ChallengeType.LATERAL
        ChallengeType.LATERAL -> ChallengeType.LOGIC
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