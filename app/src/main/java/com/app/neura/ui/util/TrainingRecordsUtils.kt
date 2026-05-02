package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.TrainingRecordItem
import com.app.neura.data.model.TrainingRecordsSummary

fun List<GameSessionResult>.buildTrainingRecordsSummary(): TrainingRecordsSummary {
    if (isEmpty()) {
        return TrainingRecordsSummary(
            highlightTitle = "No records yet",
            highlightMessage = "Complete your first session to start building personal records.",
            items = listOf(
                TrainingRecordItem(
                    title = "Total sessions",
                    value = "0",
                    description = "No completed sessions yet.",
                    icon = "🧠"
                ),
                TrainingRecordItem(
                    title = "Best accuracy",
                    value = "N/A",
                    description = "Complete a session to unlock this record.",
                    icon = "🎯"
                ),
                TrainingRecordItem(
                    title = "Perfect sessions",
                    value = "0",
                    description = "Score full marks to earn a perfect session.",
                    icon = "🏆"
                )
            )
        )
    }

    val totalSessions = size
    val totalCorrectAnswers = sumOf { it.score }
    val totalQuestions = sumOf { it.totalQuestions }
    val perfectSessions = count { it.totalQuestions > 0 && it.score == it.totalQuestions }

    val bestAccuracy = maxOfOrNull { it.accuracyPercent() } ?: 0

    val averageAccuracy = if (totalQuestions == 0) {
        0
    } else {
        ((totalCorrectAnswers.toDouble() / totalQuestions.toDouble()) * 100)
            .toInt()
            .coerceIn(0, 100)
    }

    val bestLogicAccuracy = filter { it.type == ChallengeType.LOGIC }
        .maxOfOrNull { it.accuracyPercent() }

    val bestLateralAccuracy = filter { it.type == ChallengeType.LATERAL }
        .maxOfOrNull { it.accuracyPercent() }

    val highlightTitle = when {
        perfectSessions > 0 -> "Perfect sessions unlocked"
        bestAccuracy >= 80 -> "Strong accuracy record"
        totalSessions >= 10 -> "Training consistency unlocked"
        else -> "Records started"
    }

    val highlightMessage = when {
        perfectSessions > 0 -> {
            "You have completed $perfectSessions perfect ${pluralizeSession(perfectSessions)}."
        }

        bestAccuracy >= 80 -> {
            "Your best session reached $bestAccuracy% accuracy."
        }

        totalSessions >= 10 -> {
            "You have completed $totalSessions training sessions."
        }

        else -> {
            "Keep training to unlock stronger records."
        }
    }

    return TrainingRecordsSummary(
        highlightTitle = highlightTitle,
        highlightMessage = highlightMessage,
        items = listOf(
            TrainingRecordItem(
                title = "Total sessions",
                value = totalSessions.toString(),
                description = "$totalSessions completed ${pluralizeSession(totalSessions)}.",
                icon = "🧠"
            ),
            TrainingRecordItem(
                title = "Best accuracy",
                value = "$bestAccuracy%",
                description = "Your highest accuracy in a single session.",
                icon = "🎯"
            ),
            TrainingRecordItem(
                title = "Average accuracy",
                value = "$averageAccuracy%",
                description = "Your overall accuracy across all completed sessions.",
                icon = "📊"
            ),
            TrainingRecordItem(
                title = "Correct answers",
                value = totalCorrectAnswers.toString(),
                description = "$totalCorrectAnswers correct answers out of $totalQuestions.",
                icon = "✅"
            ),
            TrainingRecordItem(
                title = "Perfect sessions",
                value = perfectSessions.toString(),
                description = "$perfectSessions perfect ${pluralizeSession(perfectSessions)} completed.",
                icon = "🏆"
            ),
            TrainingRecordItem(
                title = "Best Logic",
                value = bestLogicAccuracy?.let { "$it%" } ?: "N/A",
                description = if (bestLogicAccuracy == null) {
                    "Complete a Logic session to unlock this record."
                } else {
                    "Your best Logic session accuracy."
                },
                icon = "🧩"
            ),
            TrainingRecordItem(
                title = "Best Lateral",
                value = bestLateralAccuracy?.let { "$it%" } ?: "N/A",
                description = if (bestLateralAccuracy == null) {
                    "Complete a Lateral session to unlock this record."
                } else {
                    "Your best Lateral session accuracy."
                },
                icon = "💡"
            )
        )
    )
}

private fun GameSessionResult.accuracyPercent(): Int {
    if (totalQuestions <= 0) return 0

    return ((score.toDouble() / totalQuestions.toDouble()) * 100)
        .toInt()
        .coerceIn(0, 100)
}

private fun pluralizeSession(count: Int): String {
    return if (count == 1) "session" else "sessions"
}