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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.TrainingIdentity
import com.app.neura.data.model.UserProfile
import com.app.neura.ui.component.TopBackHeader

@Composable
fun ProfileScreen(
    profile: UserProfile,
    trainingIdentity: TrainingIdentity,
    createdChallengesCount: Int,
    savedPacksCount: Int,
    onOpenShareIdentity: () -> Unit,
    onOpenRecords: () -> Unit,
    onOpenMyChallenges: () -> Unit,
    onOpenMyPacks: () -> Unit,
    onOpenFavorites: () -> Unit,
    onOpenPlayLater: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenSettings: () -> Unit,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var displayName by remember(profile.displayName) { mutableStateOf(profile.displayName) }
    var username by remember(profile.username) { mutableStateOf(profile.username) }
    var bio by remember(profile.bio) { mutableStateOf(profile.bio) }
    var favoriteTag by remember(profile.favoriteTag) { mutableStateOf(profile.favoriteTag) }
    var weeklyGoalSessions by remember(profile.weeklyGoalSessions) {
        mutableStateOf(profile.weeklyGoalSessions)
    }
    var editMode by remember { mutableStateOf(false) }

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
                    title = "Profile",
                    subtitle = "Your Neura identity.",
                    onBack = onBack
                )
            }

            item {
                ProfileHeroCard(
                    profile = profile,
                    trainingIdentity = trainingIdentity,
                    createdChallengesCount = createdChallengesCount,
                    savedPacksCount = savedPacksCount
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { editMode = !editMode },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(if (editMode) "Close edit" else "Edit profile")
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

            item {
                TrainingIdentityCard(
                    identity = trainingIdentity,
                    onOpenRecords = onOpenRecords
                )
            }

            item {
                ProfileHubCard(
                    title = "Creator space",
                    message = "Manage what you create, save and plan to play.",
                    actions = listOf(
                        ProfileAction("My challenges", "$createdChallengesCount created", "🧩", onOpenMyChallenges),
                        ProfileAction("My packs", "$savedPacksCount saved", "📦", onOpenMyPacks),
                        ProfileAction("Favorites", "Saved picks", "⭐", onOpenFavorites),
                        ProfileAction("Play later", "Queued packs", "⏳", onOpenPlayLater)
                    )
                )
            }

            item {
                ProfileHubCard(
                    title = "Progress",
                    message = "Review records, achievements and deeper stats.",
                    actions = listOf(
                        ProfileAction("Records", "Personal bests", "🥇", onOpenRecords),
                        ProfileAction("Stats", "Session history", "📊", onOpenStats),
                        ProfileAction("Achievements", "Milestones", "🏆", onOpenAchievements),
                        ProfileAction("Settings", "App tools", "⚙️", onOpenSettings)
                    )
                )
            }

            if (editMode) {
                item {
                    EditProfileCard(
                        displayName = displayName,
                        onDisplayNameChange = { displayName = it },
                        username = username,
                        onUsernameChange = { username = it },
                        bio = bio,
                        onBioChange = { bio = it },
                        favoriteTag = favoriteTag,
                        onFavoriteTagChange = { favoriteTag = it },
                        weeklyGoalSessions = weeklyGoalSessions,
                        onWeeklyGoalChange = { weeklyGoalSessions = it },
                        onSave = {
                            onSave(
                                UserProfile(
                                    displayName = displayName.trim().ifEmpty { "You" },
                                    username = username.trim().ifEmpty { "neura_user" },
                                    bio = bio.trim(),
                                    favoriteTag = favoriteTag.trim(),
                                    weeklyGoalSessions = weeklyGoalSessions.coerceIn(1, 7)
                                )
                            )
                            editMode = false
                        }
                    )
                }
            }
        }
    }
}

private data class ProfileAction(
    val title: String,
    val subtitle: String,
    val icon: String,
    val onClick: () -> Unit
)

@Composable
private fun ProfileHeroCard(
    profile: UserProfile,
    trainingIdentity: TrainingIdentity,
    createdChallengesCount: Int,
    savedPacksCount: Int
) {
    val initials = profile.displayName
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "U" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                ) {
                    Text(
                        text = initials,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = profile.displayName.ifBlank { "You" },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "@${profile.username.ifBlank { "neura_user" }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = profile.bio.ifBlank { "Training mind, creating challenges and building progress." },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileMetric(
                    label = "Level",
                    value = trainingIdentity.level.toString(),
                    modifier = Modifier.weight(1f)
                )
                ProfileMetric(
                    label = "Challenges",
                    value = createdChallengesCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                ProfileMetric(
                    label = "Packs",
                    value = savedPacksCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TrainingIdentityCard(
    identity: TrainingIdentity,
    onOpenRecords: () -> Unit
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
                text = "Training Identity",
                style = MaterialTheme.typography.labelLarge
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
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Level ${identity.level} • ${identity.averageAccuracyText} average",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "LV ${identity.level}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Text(
                text = "${identity.mainStyleText} main style • ${identity.bestAreaText} best area",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = identity.weeklyProgressText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedButton(
                onClick = onOpenRecords,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("View records")
            }
        }
    }
}

@Composable
private fun ProfileHubCard(
    title: String,
    message: String,
    actions: List<ProfileAction>
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
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            actions.chunked(2).forEach { rowActions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowActions.forEach { action ->
                        ProfileActionButton(
                            action = action,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (rowActions.size == 1) {
                        Column(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileActionButton(
    action: ProfileAction,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = action.onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "${action.icon} ${action.title}",
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = action.subtitle,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun ProfileMetric(
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
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun EditProfileCard(
    displayName: String,
    onDisplayNameChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    favoriteTag: String,
    onFavoriteTagChange: (String) -> Unit,
    weeklyGoalSessions: Int,
    onWeeklyGoalChange: (Int) -> Unit,
    onSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Edit profile",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                label = { Text("Display name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bio,
                onValueChange = onBioChange,
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = favoriteTag,
                onValueChange = onFavoriteTagChange,
                label = { Text("Favorite tag") },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Weekly goal",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 3, 5, 7).forEach { goal ->
                    OutlinedButton(
                        onClick = { onWeeklyGoalChange(goal) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (weeklyGoalSessions == goal) "$goal ✓" else goal.toString())
                    }
                }
            }

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save profile")
            }
        }
    }
}
