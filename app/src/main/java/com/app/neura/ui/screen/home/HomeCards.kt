package com.app.neura.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.app.neura.data.model.HomeInsight
import com.app.neura.data.model.MissionBadgesSummary
import com.app.neura.data.model.TrainingPlanSummary
import com.app.neura.data.model.WeeklyMissionsSummary
import com.app.neura.ui.screen.InsightMetricChip

data class HomeTile(
    val title: String,
    val subtitle: String,
    val icon: String,
    val onClick: () -> Unit
)

@Composable
fun HomeHeader(
    dailyCompletedToday: Boolean,
    currentDailyStreak: Int,
    calmMode: Boolean,
    readingHelper: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(if (readingHelper) 18.dp else 12.dp)
        ) {
            Text(
                text = if (calmMode) "Neura" else "🧠 Neura",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Train your mind. Create challenges. Build your collection.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
            ) {
                Text(
                    text = if (dailyCompletedToday) {
                        if (calmMode) {
                            "Daily completed • Streak $currentDailyStreak"
                        } else {
                            "✅ Daily completed • 🔥 Streak $currentDailyStreak"
                        }
                    } else {
                        if (calmMode) {
                            "Daily not completed • Streak $currentDailyStreak"
                        } else {
                            "☀️ Daily waiting • 🔥 Streak $currentDailyStreak"
                        }
                    },
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun HomeTileCard(
    tile: HomeTile,
    calmMode: Boolean,
    readingHelper: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(if (readingHelper) 172.dp else 156.dp)
            .clickable { tile.onClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = tile.icon,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = if (calmMode) {
                        MaterialTheme.typography.labelLarge
                    } else {
                        MaterialTheme.typography.titleLarge
                    }
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = tile.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                Text(
                    text = tile.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TrainingPlanPreviewCard(
    plan: TrainingPlanSummary,
    calmMode: Boolean,
    onOpenTrainingPlan: () -> Unit,
    onStartRecommended: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (calmMode) {
                    "Training plan"
                } else {
                    "🧭 Training plan"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = plan.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = plan.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Focus area",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Text(
                        text = plan.focusAreaText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenTrainingPlan,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Open plan")
                }

                Button(
                    onClick = onStartRecommended,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Start now")
                }
            }
        }
    }
}

@Composable
fun WeeklyMissionsPreviewCard(
    summary: WeeklyMissionsSummary,
    calmMode: Boolean,
    onOpenMissions: () -> Unit,
    onStartPrimaryMission: () -> Unit
) {
    val previewMissions = summary.missions.take(2)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (calmMode) {
                    "Weekly missions"
                } else {
                    "✅ Weekly missions"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = summary.progressText,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = summary.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            previewMissions.forEach { mission ->
                WeeklyMissionPreviewRow(mission = mission)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenMissions,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("View")
                }

                Button(
                    onClick = onStartPrimaryMission,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Start")
                }
            }
        }
    }
}

@Composable
fun MissionBadgesPreviewCard(
    summary: MissionBadgesSummary,
    calmMode: Boolean,
    onOpenBadges: () -> Unit
) {
    val previewBadges = summary.badges.take(3)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (calmMode) {
                    "Mission badges"
                } else {
                    "🏅 Mission badges"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = summary.progressText,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = summary.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            previewBadges.forEach { badge ->
                MissionBadgePreviewRow(badge = badge)
            }

            Button(
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
fun HomeInsightCard(
    insight: HomeInsight,
    calmMode: Boolean,
    onPrimaryAction: () -> Unit
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
                text = if (calmMode) {
                    "Your next step"
                } else {
                    "✨ Your next step"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = insight.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = insight.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InsightMetricChip(
                    label = "Sessions",
                    value = insight.totalSessions.toString(),
                    modifier = Modifier.weight(1f)
                )

                InsightMetricChip(
                    label = "Average",
                    value = insight.averageAccuracyText,
                    modifier = Modifier.weight(1f)
                )

                InsightMetricChip(
                    label = "Streak",
                    value = "${insight.currentDailyStreak}d",
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = onPrimaryAction,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(insight.primaryActionLabel)
            }
        }
    }
}

@Composable
fun WeeklyMissionPreviewRow(
    mission: com.app.neura.data.model.WeeklyMission
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
                text = mission.icon,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = mission.title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = if (mission.isCompleted) {
                    "Done • ${mission.progressText}"
                } else {
                    mission.progressText
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MissionBadgePreviewRow(
    badge: com.app.neura.data.model.MissionBadge
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
                text = if (badge.isUnlocked) badge.icon else "🔒",
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
                text = if (badge.isUnlocked) {
                    "Unlocked • ${badge.description}"
                } else {
                    "Locked • ${badge.unlockHint}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}