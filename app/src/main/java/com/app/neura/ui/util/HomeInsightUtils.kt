package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.HomeInsight

fun List<GameSessionResult>.buildHomeInsight(
    dailyCompletedToday: Boolean,
    currentDailyStreak: Int,
    hasOngoingSession: Boolean
): HomeInsight {
    if (hasOngoingSession) {
        return HomeInsight(
            title = "Resume your open session",
            message = "You already have a session in progress. Continue from where you left off.",
            totalSessions = size,
            averageAccuracyText = averageAccuracyText(),
            currentDailyStreak = currentDailyStreak,
            suggestedType = mostUsefulFallbackType(),
            suggestedDifficulty = null,
            suggestedQuestionCount = 3,
            primaryActionLabel = "Continue session",
            shouldResumeSession = true
        )
    }

    if (isEmpty()) {
        return HomeInsight(
            title = "Start your first training session",
            message = "Complete a short session so Neura can begin learning what helps you most.",
            totalSessions = 0,
            averageAccuracyText = "N/A",
            currentDailyStreak = currentDailyStreak,
            suggestedType = ChallengeType.LOGIC,
            suggestedDifficulty = ChallengeDifficulty.EASY,
            suggestedQuestionCount = 3,
            primaryActionLabel = "Start suggested session",
            shouldResumeSession = false
        )
    }

    val weakestType = weakestChallengeType() ?: ChallengeType.LOGIC
    val averageAccuracy = averageAccuracyPercent()

    val suggestedDifficulty = when {
        averageAccuracy < 50 -> ChallengeDifficulty.EASY
        averageAccuracy < 75 -> ChallengeDifficulty.MEDIUM
        else -> ChallengeDifficulty.HARD
    }

    val suggestedQuestionCount = when {
        averageAccuracy < 60 -> 3
        size < 5 -> 3
        else -> 5
    }

    val title = when {
        !dailyCompletedToday -> "Keep your daily rhythm"
        weakestType == ChallengeType.LOGIC -> "Train your logic skills"
        else -> "Train your lateral thinking"
    }

    val message = when {
        !dailyCompletedToday -> {
            "Your Daily Challenge is still waiting. Complete it to keep your streak alive."
        }

        weakestType == ChallengeType.LOGIC -> {
            "Your logic results have more room to grow. A focused session is recommended."
        }

        else -> {
            "Your lateral thinking results have more room to grow. A focused session is recommended."
        }
    }

    return HomeInsight(
        title = title,
        message = message,
        totalSessions = size,
        averageAccuracyText = averageAccuracyText(),
        currentDailyStreak = currentDailyStreak,
        suggestedType = weakestType,
        suggestedDifficulty = suggestedDifficulty,
        suggestedQuestionCount = suggestedQuestionCount,
        primaryActionLabel = "Start suggested session",
        shouldResumeSession = false
    )
}

private fun List<GameSessionResult>.averageAccuracyPercent(): Int {
    if (isEmpty()) return 0

    val average = map {
        if (it.totalQuestions == 0) {
            0.0
        } else {
            it.score.toDouble() / it.totalQuestions.toDouble()
        }
    }.average()

    return (average * 100).toInt()
}

private fun List<GameSessionResult>.averageAccuracyText(): String {
    if (isEmpty()) return "N/A"

    return "${averageAccuracyPercent()}%"
}

private fun List<GameSessionResult>.weakestChallengeType(): ChallengeType? {
    return filter { it.type != null && it.totalQuestions > 0 }
        .groupBy { it.type }
        .mapNotNull { entry ->
            val type = entry.key ?: return@mapNotNull null

            val average = entry.value.map {
                it.score.toDouble() / it.totalQuestions.toDouble()
            }.average()

            type to average
        }
        .minByOrNull { it.second }
        ?.first
}

private fun List<GameSessionResult>.mostUsefulFallbackType(): ChallengeType {
    return weakestChallengeType() ?: ChallengeType.LOGIC
}