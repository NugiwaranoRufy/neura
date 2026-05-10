package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.PostSessionCoachSummary
import com.app.neura.data.model.TrainingPlanSummary
import kotlin.math.roundToInt

fun buildPostSessionCoachSummary(
    latestResult: GameSessionResult?,
    trainingPlanSummary: TrainingPlanSummary
): PostSessionCoachSummary {
    if (latestResult == null || latestResult.totalQuestions <= 0) {
        return PostSessionCoachSummary(
            title = "Start with a short session",
            message = "Complete a first round so Neura can coach your next step.",
            primaryActionLabel = "Start training",
            secondaryActionLabel = "Open plan",
            recommendedConfig = GameSessionConfig(
                type = trainingPlanSummary.recommendedType,
                totalQuestions = trainingPlanSummary.recommendedQuestionCount,
                difficulty = trainingPlanSummary.recommendedDifficulty
            ),
            shouldReviewAnswersFirst = false,
            focusLabel = trainingPlanSummary.focusAreaText,
            scoreLabel = "No result yet"
        )
    }

    val accuracy = latestResult.accuracyPercent()
    val sessionType = latestResult.type ?: trainingPlanSummary.recommendedType
    val nextDifficulty = nextDifficultyForAccuracy(accuracy, trainingPlanSummary.recommendedDifficulty)
    val nextQuestionCount = nextQuestionCountForAccuracy(accuracy, latestResult.totalQuestions)

    return when {
        accuracy >= 90 -> PostSessionCoachSummary(
            title = "Push your streak forward",
            message = "Great result. Try a slightly longer or harder session while your momentum is high.",
            primaryActionLabel = "Train next",
            secondaryActionLabel = "Open plan",
            recommendedConfig = GameSessionConfig(
                type = sessionType,
                totalQuestions = nextQuestionCount,
                difficulty = nextDifficulty
            ),
            shouldReviewAnswersFirst = false,
            focusLabel = sessionType.toCoachText(),
            scoreLabel = "$accuracy% score"
        )

        accuracy >= 60 -> PostSessionCoachSummary(
            title = "Build on this session",
            message = "You have a good base. Review quickly, then repeat the focus area to consolidate it.",
            primaryActionLabel = "Review first",
            secondaryActionLabel = "Open plan",
            recommendedConfig = GameSessionConfig(
                type = sessionType,
                totalQuestions = latestResult.totalQuestions.coerceAtLeast(3),
                difficulty = trainingPlanSummary.recommendedDifficulty
            ),
            shouldReviewAnswersFirst = true,
            focusLabel = sessionType.toCoachText(),
            scoreLabel = "$accuracy% score"
        )

        else -> PostSessionCoachSummary(
            title = "Recover with a lighter round",
            message = "This was useful practice. Review the explanations, then try a shorter easy session.",
            primaryActionLabel = "Review answers",
            secondaryActionLabel = "Open plan",
            recommendedConfig = GameSessionConfig(
                type = sessionType,
                totalQuestions = 3,
                difficulty = ChallengeDifficulty.EASY
            ),
            shouldReviewAnswersFirst = true,
            focusLabel = sessionType.toCoachText(),
            scoreLabel = "$accuracy% score"
        )
    }
}

private fun GameSessionResult.accuracyPercent(): Int {
    if (totalQuestions <= 0) return 0

    return ((score.toDouble() / totalQuestions.toDouble()) * 100)
        .roundToInt()
        .coerceIn(0, 100)
}

private fun nextQuestionCountForAccuracy(
    accuracy: Int,
    previousTotal: Int
): Int {
    return when {
        accuracy >= 90 -> if (previousTotal >= 5) 5 else 5
        accuracy >= 60 -> previousTotal.coerceIn(3, 5)
        else -> 3
    }
}

private fun nextDifficultyForAccuracy(
    accuracy: Int,
    fallback: ChallengeDifficulty?
): ChallengeDifficulty? {
    return when {
        accuracy >= 90 -> ChallengeDifficulty.HARD
        accuracy >= 60 -> fallback ?: ChallengeDifficulty.MEDIUM
        else -> ChallengeDifficulty.EASY
    }
}

private fun ChallengeType.toCoachText(): String {
    return when (this) {
        ChallengeType.LOGIC -> "Logic"
        ChallengeType.LATERAL -> "Lateral thinking"
    }
}
