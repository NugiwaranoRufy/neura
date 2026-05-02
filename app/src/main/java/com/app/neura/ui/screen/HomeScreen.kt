package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.data.model.ActivityFeedItem
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.GameSessionConfig
import com.app.neura.data.model.HomeSmartSummary
import com.app.neura.data.model.MissionBadgesSummary
import com.app.neura.data.model.TrainingIdentity
import com.app.neura.data.model.TrainingRecordsSummary
import com.app.neura.data.model.WeeklyGoalProgress
import com.app.neura.data.model.WeeklyMissionsSummary
import com.app.neura.ui.component.SelectableOptionButton
import com.app.neura.ui.screen.home.HomeHeader
import com.app.neura.ui.screen.home.HomeSmartCard
import com.app.neura.ui.screen.home.HomeTile
import com.app.neura.ui.screen.home.HomeTileCard
import com.app.neura.ui.screen.home.MissionBadgesCompactCard
import com.app.neura.ui.screen.home.WeeklyMissionsCompactCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onStartSession: (GameSessionConfig) -> Unit,
    onResumeSession: () -> Unit,
    hasOngoingSession: Boolean,
    homeSmartSummary: HomeSmartSummary,
    weeklyMissionsSummary: WeeklyMissionsSummary,
    missionBadgesSummary: MissionBadgesSummary,
    onOpenTrainingPlan: () -> Unit,
    onOpenMissions: () -> Unit,
    onOpenBadges: () -> Unit,
    onCreateChallenge: () -> Unit,
    onOpenMyChallenges: () -> Unit,
    onOpenSettings: () -> Unit,
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
    val recordsIcon = if (accessibilitySettings.calmMode) "Records" else "🥇"
    val statsIcon = if (accessibilitySettings.calmMode) "Stats" else "📊"
    val achievementsIcon = if (accessibilitySettings.calmMode) "Goals" else "🏆"
    val roomDebugIcon = if (accessibilitySettings.calmMode) "Debug" else "🛠️"
    val accessibilityIcon = if (accessibilitySettings.calmMode) "Access" else "♿"
    val settingsIcon = if (accessibilitySettings.calmMode) "Settings" else "⚙️"
    val planIcon = if (accessibilitySettings.calmMode) "Plan" else "🧭"
    val missionsIcon = if (accessibilitySettings.calmMode) "Missions" else "✅"
    val badgesIcon = if (accessibilitySettings.calmMode) "Badges" else "🏅"


    val tiles = remember(
        userChallengeCount,
        accessibilitySettings.calmMode,
        onCreateChallenge,
        onOpenMyChallenges,
        onOpenSettings
    ) {

        listOf(

            HomeTile(
                title = "Create",
                subtitle = "New challenge",
                icon = if (accessibilitySettings.calmMode) "Create" else "➕",
                onClick = onCreateChallenge
            ),
            HomeTile(
                title = "My challenges",
                subtitle = "$userChallengeCount created",
                icon = if (accessibilitySettings.calmMode) "Challenges" else "🧩",
                onClick = onOpenMyChallenges
            ),
            HomeTile(
                title = "Settings",
                subtitle = "App tools",
                icon = if (accessibilitySettings.calmMode) "Settings" else "⚙️",
                onClick = onOpenSettings
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            HomeSmartCard(
                summary = homeSmartSummary,
                calmMode = accessibilitySettings.calmMode,
                onPrimaryAction = {
                    if (homeSmartSummary.shouldResumeSession) {
                        onResumeSession()
                    } else {
                        onStartSession(homeSmartSummary.recommendedConfig)
                    }
                },
                onSecondaryAction = {
                    onOpenTrainingPlan()
                }
            )

            WeeklyMissionsCompactCard(
                summary = weeklyMissionsSummary,
                calmMode = accessibilitySettings.calmMode,
                onOpenMissions = onOpenMissions
            )

            MissionBadgesCompactCard(
                summary = missionBadgesSummary,
                calmMode = accessibilitySettings.calmMode,
                onOpenBadges = onOpenBadges
            )

            OutlinedButton(
                onClick = {
                    showSessionSetup = !showSessionSetup
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(if (showSessionSetup) "Hide custom session" else "Custom session")
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

                Button(
                    onClick = {
                        onStartSession(
                            GameSessionConfig(
                                type = selectedType,
                                totalQuestions = selectedCount,
                                difficulty = selectedDifficulty
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Start custom session")
                }
            }

            Text(
                text = "Quick actions",
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