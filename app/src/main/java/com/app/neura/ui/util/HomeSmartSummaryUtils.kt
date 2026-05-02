package com.app.neura.ui.util

import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.GameSessionResult
import com.app.neura.data.model.HomeSmartSummary
import com.app.neura.data.model.TrainingPlanSummary
import com.app.neura.data.model.WeeklyMissionsSummary

fun List<GameSessionResult>.buildHomeSmartSummary(
    hasOngoingSession: Boolean,
    dailyCompletedToday: Boolean,
    trainingPlanSummary: TrainingPlanSummary,
    weeklyMissionsSummary: WeeklyMissionsSummary
): HomeSmartSummary {
    if (hasOngoingSession) {
        return HomeSmartSummary(
            title = "Continue where you left off",
            message = "You have an open session ready to resume.",
            primaryActionLabel = "Resume",
            secondaryActionLabel = "View plan",
            recommendedConfig = GameSessionConfig(
                type = trainingPlanSummary.recommendedType,
                totalQuestions = trainingPlanSummary.recommendedQuestionCount,
                difficulty = trainingPlanSummary.recommendedDifficulty
            ),
            shouldResumeSession = true,
            focusLabel = "Open session",
            progressLabel = weeklyMissionsSummary.progressText
        )
    }

    val firstOpenMission = weeklyMissionsSummary.missions.firstOrNull { !it.isCompleted }
    val recommendedConfig = if (firstOpenMission != null) {
        GameSessionConfig(
            type = firstOpenMission.recommendedType,
            totalQuestions = firstOpenMission.recommendedQuestionCount,
            difficulty = firstOpenMission.recommendedDifficulty
        )
    } else {
        GameSessionConfig(
            type = trainingPlanSummary.recommendedType,
            totalQuestions = trainingPlanSummary.recommendedQuestionCount,
            difficulty = trainingPlanSummary.recommendedDifficulty
        )
    }

    if (isEmpty()) {
        return HomeSmartSummary(
            title = "Start your Neura journey",
            message = "Complete a short session so Neura can personalize your training.",
            primaryActionLabel = "Start",
            secondaryActionLabel = "Discover",
            recommendedConfig = GameSessionConfig(
                type = ChallengeType.LOGIC,
                totalQuestions = 3,
                difficulty = ChallengeDifficulty.EASY
            ),
            shouldResumeSession = false,
            focusLabel = "First session",
            progressLabel = "No sessions yet"
        )
    }

    if (!dailyCompletedToday) {
        return HomeSmartSummary(
            title = "Keep today’s rhythm",
            message = "Start with a short session to keep your training momentum alive.",
            primaryActionLabel = "Train now",
            secondaryActionLabel = "View missions",
            recommendedConfig = recommendedConfig,
            shouldResumeSession = false,
            focusLabel = trainingPlanSummary.focusAreaText,
            progressLabel = weeklyMissionsSummary.progressText
        )
    }

    return HomeSmartSummary(
        title = trainingPlanSummary.title,
        message = trainingPlanSummary.message,
        primaryActionLabel = "Start next",
        secondaryActionLabel = "View plan",
        recommendedConfig = recommendedConfig,
        shouldResumeSession = false,
        focusLabel = trainingPlanSummary.focusAreaText,
        progressLabel = weeklyMissionsSummary.progressText
    )
}