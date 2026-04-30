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