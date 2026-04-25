package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig

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
    onCreateChallenge: () -> Unit,
    onOpenMyChallenges: () -> Unit,
    onOpenTransfer: () -> Unit,
    onOpenMyPacks: () -> Unit,
    onOpenDiscover: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenPlayLater: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenRoomDebug: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onStartDailyChallenge: () -> Unit,
    userChallengeCount: Int,
    dailyCompletedToday: Boolean,
    currentDailyStreak: Int
) {
    var selectedType by remember { mutableStateOf(ChallengeType.LOGIC) }
    var selectedDifficulty by remember { mutableStateOf<ChallengeDifficulty?>(null) }
    var selectedCount by remember { mutableIntStateOf(3) }
    var showSessionSetup by remember { mutableStateOf(false) }

    val tiles = listOf(
        HomeTile(
            title = if (dailyCompletedToday) "Daily completed" else "Daily challenge",
            subtitle = "One challenge for today",
            icon = "☀️",
            onClick = onStartDailyChallenge
        ),
        HomeTile(
            title = "Create",
            subtitle = "Build a new challenge",
            icon = "✍️",
            onClick = onCreateChallenge
        ),
        HomeTile(
            title = "My challenges",
            subtitle = "$userChallengeCount created",
            icon = "🧩",
            onClick = onOpenMyChallenges
        ),
        HomeTile(
            title = "My packs",
            subtitle = "Saved collections",
            icon = "📦",
            onClick = onOpenMyPacks
        ),
        HomeTile(
            title = "Discover",
            subtitle = "Featured packs",
            icon = "🌍",
            onClick = onOpenDiscover
        ),
        HomeTile(
            title = "Favorites",
            subtitle = "Your saved picks",
            icon = "⭐",
            onClick = onOpenFavorites
        ),
        HomeTile(
            title = "Play later",
            subtitle = "Queued packs",
            icon = "⏳",
            onClick = onOpenPlayLater
        ),
        HomeTile(
            title = "Import / Export",
            subtitle = "Share content",
            icon = "🔁",
            onClick = onOpenTransfer
        ),
        HomeTile(
            title = "Profile",
            subtitle = "Author profile",
            icon = "👤",
            onClick = onOpenProfile
        ),
        HomeTile(
            title = "Stats",
            subtitle = "Progress history",
            icon = "📊",
            onClick = onOpenStats
        ),
        HomeTile(
            title = "Achievements",
            subtitle = "Milestones",
            icon = "🏆",
            onClick = onOpenAchievements
        ),
        HomeTile(
            title = "Room debug",
            subtitle = "Database tools",
            icon = "🛠️",
            onClick = onOpenRoomDebug
        )
    )

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
                    currentDailyStreak = currentDailyStreak
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
                            modifier = Modifier
                                .weight(1f)
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
    currentDailyStreak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Neura",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Micro mental challenges",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = if (dailyCompletedToday) {
                    "Daily completed today • Streak: $currentDailyStreak"
                } else {
                    "Daily not completed today • Streak: $currentDailyStreak"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun HomeTileCard(
    tile: HomeTile,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(132.dp)
            .clickable { tile.onClick() },
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = tile.icon,
                style = MaterialTheme.typography.headlineSmall
            )

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
                    maxLines = 2
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
private fun SelectableOptionButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(if (selected) "• $text" else text)
    }
}