package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.BadgeUnlockSummary
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.MissionBadge
import com.app.neura.data.model.PostSessionCoachSummary

@Composable
fun ResultScreen(
    score: Int,
    total: Int,
    badgeUnlockSummary: BadgeUnlockSummary,
    postSessionCoachSummary: PostSessionCoachSummary,
    onOpenBadges: () -> Unit,
    onOpenTrainingPlan: () -> Unit,
    onStartCoachSession: (GameSessionConfig) -> Unit,
    onReviewAnswers: () -> Unit,
    onPlayAgain: () -> Unit
) {
    val percentage = if (total == 0) 0 else (score * 100) / total

    val title = when {
        percentage == 100 -> "Perfect mind"
        percentage >= 70 -> "Great session"
        percentage >= 40 -> "Good training"
        else -> "Keep practicing"
    }

    val badge = when {
        percentage == 100 -> "🏆 Perfect"
        percentage >= 70 -> "🔥 Strong"
        percentage >= 40 -> "💪 Progress"
        else -> "🌱 Learning"
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Session complete",
                style = MaterialTheme.typography.headlineMedium
            )

            ResultScoreCard(
                badge = badge,
                title = title,
                score = score,
                total = total,
                percentage = percentage
            )

            PostSessionCoachCard(
                summary = postSessionCoachSummary,
                onPrimaryAction = {
                    if (postSessionCoachSummary.shouldReviewAnswersFirst) {
                        onReviewAnswers()
                    } else {
                        onStartCoachSession(postSessionCoachSummary.recommendedConfig)
                    }
                },
                onSecondaryAction = onOpenTrainingPlan
            )

            BadgeUnlockCard(
                summary = badgeUnlockSummary,
                onOpenBadges = onOpenBadges
            )

            OutlinedButton(
                onClick = onReviewAnswers,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Review answers")
            }

            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
private fun ResultScoreCard(
    badge: String,
    title: String,
    score: Int,
    total: Int,
    percentage: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "$score / $total",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "$percentage% score",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PostSessionCoachCard(
    summary: PostSessionCoachSummary,
    onPrimaryAction: () -> Unit,
    onSecondaryAction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "🧭 Coach",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = summary.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = summary.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CoachChip(
                    label = "Focus",
                    value = summary.focusLabel,
                    modifier = Modifier.weight(1f)
                )

                CoachChip(
                    label = "Result",
                    value = summary.scoreLabel,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPrimaryAction,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(summary.primaryActionLabel)
                }

                OutlinedButton(
                    onClick = onSecondaryAction,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(summary.secondaryActionLabel)
                }
            }
        }
    }
}

@Composable
private fun CoachChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }
    }
}

@Composable
private fun BadgeUnlockCard(
    summary: BadgeUnlockSummary,
    onOpenBadges: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (summary.unlockedBadges.isEmpty()) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (summary.unlockedBadges.isEmpty()) {
                    "Badge progress"
                } else {
                    "🏅 ${summary.title}"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = summary.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (summary.unlockedBadges.isNotEmpty()) {
                summary.unlockedBadges.take(3).forEach { badge ->
                    BadgeUnlockedRow(badge = badge)
                }
            }

            OutlinedButton(
                onClick = onOpenBadges,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("View badges")
            }
        }
    }
}

@Composable
private fun BadgeUnlockedRow(
    badge: MissionBadge
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = badge.icon,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = badge.title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = badge.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
