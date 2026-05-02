package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.TrainingPlanStep
import com.app.neura.data.model.TrainingPlanSummary
import com.app.neura.ui.component.TopBackHeader

@Composable
fun TrainingPlanScreen(
    plan: TrainingPlanSummary,
    onStartPlanStep: (GameSessionConfig) -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TopBackHeader(
                    title = "Training Plan",
                    subtitle = "Your personalized path for this week.",
                    onBack = onBack
                )
            }

            item {
                TrainingPlanHeaderCard(plan = plan)
            }

            itemsIndexed(plan.steps) { index, step ->
                TrainingPlanStepCard(
                    index = index + 1,
                    step = step,
                    onStart = {
                        onStartPlanStep(
                            GameSessionConfig(
                                type = step.type,
                                totalQuestions = step.questionCount,
                                difficulty = step.difficulty
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TrainingPlanHeaderCard(
    plan: TrainingPlanSummary
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Adaptive coach",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = plan.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = plan.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Focus area",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = plan.focusAreaText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun TrainingPlanStepCard(
    index: Int,
    step: TrainingPlanStep,
    onStart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (step.isPrimary) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = index.toString(),
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = step.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = step.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Text(
                text = "${step.type.toReadableText()} • ${step.difficulty.toReadableText()} • ${step.questionCount} questions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(step.actionLabel)
            }
        }
    }
}

private fun com.app.neura.data.model.ChallengeType.toReadableText(): String {
    return when (this) {
        com.app.neura.data.model.ChallengeType.LOGIC -> "Logic"
        com.app.neura.data.model.ChallengeType.LATERAL -> "Lateral"
    }
}

private fun ChallengeDifficulty?.toReadableText(): String {
    return when (this) {
        ChallengeDifficulty.EASY -> "Easy"
        ChallengeDifficulty.MEDIUM -> "Medium"
        ChallengeDifficulty.HARD -> "Hard"
        null -> "All difficulties"
    }
}