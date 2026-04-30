package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.ui.component.SelectableOptionButton
import com.app.neura.data.model.HomeInsight
import com.app.neura.data.model.WeeklyGoalProgress
import com.app.neura.data.model.ActivityFeedItem
import com.app.neura.data.model.TrainingIdentity

private data class HomeTile(
    val title: String,
    val subtitle: String,
    val icon: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onStartSession: (GameSessionConfig) -> Unit,
    onResumeSession: () -> Unit,
    hasOngoingSession: Boolean,
    homeInsight: HomeInsight,
    weeklyGoalProgress: WeeklyGoalProgress,
    trainingIdentity: TrainingIdentity,
    recentActivityItems: List<ActivityFeedItem>,
    onOpenActivity: () -> Unit,
    onOpenShareIdentity: () -> Unit,
    onCreateChallenge: () -> Unit,
    onOpenMyChallenges: () -> Unit,
    onOpenTransfer: () -> Unit,
    onOpenMyPacks: () -> Unit,
    onOpenDiscover: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenPlayLater: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenRoomDebug: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onStartDailyChallenge: () -> Unit,
    userChallengeCount: Int,
    dailyCompletedToday: Boolean,
    currentDailyStreak: Int,
    accessibilitySettings: AccessibilitySettings
) {
    var selectedType by remember { mutableStateOf(ChallengeType.LOGIC) }
    var selectedDifficulty by remember { mutableStateOf<ChallengeDifficulty?>(null) }
    var selectedCount by remember { mutableIntStateOf(3) }
    var showSessionSetup by remember { mutableStateOf(false) }

    val dailyIcon = if (accessibilitySettings.calmMode) "Daily" else "☀️"
    val createIcon = if (accessibilitySettings.calmMode) "Create" else "✍️"
    val challengesIcon = if (accessibilitySettings.calmMode) "Challenges" else "🧩"
    val packsIcon = if (accessibilitySettings.calmMode) "Packs" else "📦"
    val discoverIcon = if (accessibilitySettings.calmMode) "Discover" else "🌍"
    val favoritesIcon = if (accessibilitySettings.calmMode) "Favorites" else "⭐"
    val playLaterIcon = if (accessibilitySettings.calmMode) "Later" else "⏳"
    val transferIcon = if (accessibilitySettings.calmMode) "Transfer" else "🔁"
    val profileIcon = if (accessibilitySettings.calmMode) "Profile" else "👤"
    val activityIcon = if (accessibilitySettings.calmMode) "Activity" else "📰"
    val statsIcon = if (accessibilitySettings.calmMode) "Stats" else "📊"
    val achievementsIcon = if (accessibilitySettings.calmMode) "Goals" else "🏆"
    val roomDebugIcon = if (accessibilitySettings.calmMode) "Debug" else "🛠️"
    val accessibilityIcon = if (accessibilitySettings.calmMode) "Access" else "♿"
    val settingsIcon = if (accessibilitySettings.calmMode) "Settings" else "⚙️"


    val tiles = remember(
        dailyCompletedToday,
        userChallengeCount,
        accessibilitySettings.calmMode
    ) {
        listOf(
            HomeTile(
                title = if (dailyCompletedToday) "Daily done" else "Daily",
                subtitle = "Today’s challenge",
                icon = dailyIcon,
                onClick = onStartDailyChallenge
            ),
            HomeTile(
                title = "Create",
                subtitle = "New challenge",
                icon = createIcon,
                onClick = onCreateChallenge
            ),
            HomeTile(
                title = "My challenges",
                subtitle = "$userChallengeCount created",
                icon = challengesIcon,
                onClick = onOpenMyChallenges
            ),
            HomeTile(
                title = "My packs",
                subtitle = "Collections",
                icon = packsIcon,
                onClick = onOpenMyPacks
            ),
            HomeTile(
                title = "Discover",
                subtitle = "Featured packs",
                icon = discoverIcon,
                onClick = onOpenDiscover
            ),
            HomeTile(
                title = "Favorites",
                subtitle = "Saved picks",
                icon = favoritesIcon,
                onClick = onOpenFavorites
            ),
            HomeTile(
                title = "Play later",
                subtitle = "Queued packs",
                icon = playLaterIcon,
                onClick = onOpenPlayLater
            ),
            HomeTile(
                title = "Activity",
                subtitle = "Recent progress",
                icon = activityIcon,
                onClick = onOpenActivity
            ),
            HomeTile(
                title = "Settings",
                subtitle = "App tools",
                icon = settingsIcon,
                onClick = onOpenSettings
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                HomeHeader(
                    dailyCompletedToday = dailyCompletedToday,
                    currentDailyStreak = currentDailyStreak,
                    calmMode = accessibilitySettings.calmMode,
                    readingHelper = accessibilitySettings.readingHelper
                )

                if (hasOngoingSession) {
                    ResumeSessionCard(
                        calmMode = accessibilitySettings.calmMode,
                        onResumeSession = onResumeSession
                    )
                }

                HomeInsightCard(
                    insight = homeInsight,
                    calmMode = accessibilitySettings.calmMode,
                    onPrimaryAction = {
                        if (homeInsight.shouldResumeSession) {
                            onResumeSession()
                        } else {
                            onStartSession(
                                GameSessionConfig(
                                    type = homeInsight.suggestedType,
                                    totalQuestions = homeInsight.suggestedQuestionCount,
                                    difficulty = homeInsight.suggestedDifficulty
                                )
                            )
                        }
                    }
                )

                WeeklyGoalCard(
                    progress = weeklyGoalProgress,
                    calmMode = accessibilitySettings.calmMode,
                    onStartTraining = {
                        onStartSession(
                            GameSessionConfig(
                                type = homeInsight.suggestedType,
                                totalQuestions = homeInsight.suggestedQuestionCount,
                                difficulty = homeInsight.suggestedDifficulty
                            )
                        )
                    }
                )

                TrainingIdentityPreviewCard(
                    identity = trainingIdentity,
                    calmMode = accessibilitySettings.calmMode,
                    onOpenProfile = onOpenProfile,
                    onOpenShareIdentity = onOpenShareIdentity
                )

                RecentActivityPreviewCard(
                    items = recentActivityItems,
                    calmMode = accessibilitySettings.calmMode,
                    onOpenActivity = onOpenActivity
                )

                if (showSessionSetup) {
                    SessionSetupPanel(
                        selectedType = selectedType,
                        onSelectedTypeChange = { selectedType = it },
                        selectedDifficulty = selectedDifficulty,
                        onSelectedDifficultyChange = { selectedDifficulty = it },
                        selectedCount = selectedCount,
                        onSelectedCountChange = { selectedCount = it }
                    )
                }

                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.titleLarge
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = 2
                ) {
                    tiles.forEach { tile ->
                        HomeTileCard(
                            tile = tile,
                            calmMode = accessibilitySettings.calmMode,
                            readingHelper = accessibilitySettings.readingHelper,
                            modifier = Modifier.weight(1f)

                        )
                    }

                    if (tiles.size % 2 != 0) {
                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (!showSessionSetup) {
                            showSessionSetup = true
                        } else {
                            onStartSession(
                                GameSessionConfig(
                                    type = selectedType,
                                    totalQuestions = selectedCount,
                                    difficulty = selectedDifficulty
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(if (showSessionSetup) "Start session" else "Start")
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
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
private fun HomeTileCard(
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
private fun SessionSetupPanel(
    selectedType: ChallengeType,
    onSelectedTypeChange: (ChallengeType) -> Unit,
    selectedDifficulty: ChallengeDifficulty?,
    onSelectedDifficultyChange: (ChallengeDifficulty?) -> Unit,
    selectedCount: Int,
    onSelectedCountChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Session setup",
                style = MaterialTheme.typography.titleLarge
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Category", style = MaterialTheme.typography.titleMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SelectableOptionButton(
                        text = "Logic",
                        selected = selectedType == ChallengeType.LOGIC,
                        onClick = { onSelectedTypeChange(ChallengeType.LOGIC) },
                        modifier = Modifier.weight(1f)
                    )

                    SelectableOptionButton(
                        text = "Lateral",
                        selected = selectedType == ChallengeType.LATERAL,
                        onClick = { onSelectedTypeChange(ChallengeType.LATERAL) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Difficulty", style = MaterialTheme.typography.titleMedium)

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableOptionButton(
                            text = "All",
                            selected = selectedDifficulty == null,
                            onClick = { onSelectedDifficultyChange(null) },
                            modifier = Modifier.weight(1f)
                        )

                        SelectableOptionButton(
                            text = "Easy",
                            selected = selectedDifficulty == ChallengeDifficulty.EASY,
                            onClick = { onSelectedDifficultyChange(ChallengeDifficulty.EASY) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SelectableOptionButton(
                            text = "Medium",
                            selected = selectedDifficulty == ChallengeDifficulty.MEDIUM,
                            onClick = { onSelectedDifficultyChange(ChallengeDifficulty.MEDIUM) },
                            modifier = Modifier.weight(1f)
                        )

                        SelectableOptionButton(
                            text = "Hard",
                            selected = selectedDifficulty == ChallengeDifficulty.HARD,
                            onClick = { onSelectedDifficultyChange(ChallengeDifficulty.HARD) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Session length", style = MaterialTheme.typography.titleMedium)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SelectableOptionButton(
                        text = "3",
                        selected = selectedCount == 3,
                        onClick = { onSelectedCountChange(3) },
                        modifier = Modifier.weight(1f)
                    )

                    SelectableOptionButton(
                        text = "5",
                        selected = selectedCount == 5,
                        onClick = { onSelectedCountChange(5) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumeSessionCard(
    calmMode: Boolean,
    onResumeSession: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onResumeSession() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (calmMode) {
                    "Resume session"
                } else {
                    "▶️ Resume session"
                },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Text(
                text = "Continue from where you left off.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun HomeInsightCard(
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
private fun InsightMetricChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
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
private fun WeeklyGoalCard(
    progress: WeeklyGoalProgress,
    calmMode: Boolean,
    onStartTraining: () -> Unit
) {
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
                    progress.title
                } else {
                    "🎯 ${progress.title}"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "${progress.completedSessions} of ${progress.goalSessions} sessions completed",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = progress.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = "${progress.progressPercent}%",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            Button(
                onClick = onStartTraining,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(progress.actionLabel)
            }
        }
    }
}

@Composable
private fun RecentActivityPreviewCard(
    items: List<ActivityFeedItem>,
    calmMode: Boolean,
    onOpenActivity: () -> Unit
) {
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
                    "Recent activity"
                } else {
                    "📰 Recent activity"
                },
                style = MaterialTheme.typography.titleLarge
            )

            if (items.isEmpty()) {
                Text(
                    text = "Complete a session to start building your activity timeline.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                items.take(3).forEach { item ->
                    RecentActivityPreviewRow(item = item)
                }
            }

            Button(
                onClick = onOpenActivity,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("View all activity")
            }
        }
    }
}

@Composable
private fun RecentActivityPreviewRow(
    item: ActivityFeedItem
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
                text = item.icon,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "${item.message} • ${item.metadata}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TrainingIdentityPreviewCard(
    identity: TrainingIdentity,
    calmMode: Boolean,
    onOpenProfile: () -> Unit,
    onOpenShareIdentity: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = if (calmMode) {
                    "Training Identity"
                } else {
                    "🪪 Training Identity"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = identity.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "Level ${identity.level} • ${identity.averageAccuracyText} average",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                ) {
                    Text(
                        text = "LV ${identity.level}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = "${identity.mainStyleText} main style • ${identity.bestAreaText} best area",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenProfile,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Profile")
                }

                Button(
                    onClick = onOpenShareIdentity,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Share")
                }
            }
        }
    }
}