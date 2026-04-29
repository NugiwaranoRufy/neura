package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.ui.component.SecondaryActionButton
import com.app.neura.data.model.AchievementProgress
import com.app.neura.ui.component.TopBackHeader

private data class AchievementItem(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val progressText: String
)

@Composable
fun AchievementsScreen(
    progress: AchievementProgress,
    createdChallengesCount: Int,
    savedPacksCount: Int,
    favoriteChallengesCount: Int,
    favoritePacksCount: Int,
    currentDailyStreak: Int,
    onBack: () -> Unit
) {

    val totalFavorites = favoriteChallengesCount + favoritePacksCount
    val completedSessions = progress.lifetimeSessions
    val perfectScores = progress.lifetimePerfectScores
    fun capped(value: Int, target: Int): String {
        return "${minOf(value, target)} / $target"
    }

    val achievements = listOf(
        AchievementItem(
            title = "First steps",
            description = "Complete your first session.",
            unlocked = completedSessions >= 1,
            progressText = capped(completedSessions, 1)
        ),
        AchievementItem(
            title = "Daily rhythm",
            description = "Complete the Daily challenge for 3 days in a row.",
            unlocked = progress.bestDailyStreak >= 3,
            progressText = capped(progress.bestDailyStreak, 3)
        ),
        AchievementItem(
            title = "Training habit",
            description = "Complete 5 sessions.",
            unlocked = completedSessions >= 5,
            progressText = capped(completedSessions, 5)
        ),
        AchievementItem(
            title = "Perfect mind",
            description = "Complete a session with a perfect score.",
            unlocked = perfectScores >= 1,
            progressText = capped(perfectScores, 1)
        ),
        AchievementItem(
            title = "Creator",
            description = "Create your first custom challenge.",
            unlocked = createdChallengesCount >= 1,
            progressText = capped(createdChallengesCount, 1)
        ),
        AchievementItem(
            title = "Collector",
            description = "Save or import your first pack.",
            unlocked = savedPacksCount >= 1,
            progressText = capped(savedPacksCount, 1)
        ),
        AchievementItem(
            title = "Curator",
            description = "Add at least 3 favorites.",
            unlocked = totalFavorites >= 3,
            progressText = capped(totalFavorites, 3)
        )
    )

    val unlockedCount = achievements.count { it.unlocked }
    val achievementCompletionProgress = if (achievements.isEmpty()) {
        0f
    } else {
        unlockedCount.toFloat() / achievements.size.toFloat()
    }

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
                    title = "Achievements",
                    subtitle = "Milestones unlocked through your activity.",
                    onBack = onBack
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "$unlockedCount / ${achievements.size} unlocked",
                            style = MaterialTheme.typography.titleMedium
                        )

                        LinearProgressIndicator(
                            progress = { achievementCompletionProgress },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            items(achievements, key = { it.title }) { achievement ->
                AchievementCard(achievement)
            }

        }
    }
}

@Composable
private fun AchievementCard(
    achievement: AchievementItem
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (achievement.unlocked) {
                    "✅ ${achievement.title}"
                } else {
                    "🔒 ${achievement.title}"
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = achievement.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = achievement.progressText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}