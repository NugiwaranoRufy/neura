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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.app.neura.data.model.UserProfile
import com.app.neura.ui.component.TopBackHeader
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.OutlinedButton
import com.app.neura.data.model.TrainingIdentity
import androidx.compose.material3.CardDefaults

@Composable
fun ProfileScreen(
    profile: UserProfile,
    trainingIdentity: TrainingIdentity,
    createdChallengesCount: Int,
    savedPacksCount: Int,
    onOpenShareIdentity: () -> Unit,
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


    val initials = displayName
        .trim()
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TopBackHeader(
                    title = "Profile",
                    subtitle = "Manage your author identity.",
                    onBack = onBack
                )
            }

            item {
                TrainingIdentityCard(
                    identity = trainingIdentity,
                    onOpenShareIdentity = onOpenShareIdentity
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = if (initials.isNotEmpty()) initials else "U",
                            style = MaterialTheme.typography.headlineLarge
                        )
                        Text(
                            text = displayName.ifBlank { "You" },
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "@${username.ifBlank { "neura_user" }}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = favoriteTag,
                    onValueChange = { favoriteTag = it },
                    label = { Text("Favorite tag") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Weekly goal",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Choose how many training sessions you want to complete every week.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(1, 3, 5, 7).forEach { goal ->
                                OutlinedButton(
                                    onClick = { weeklyGoalSessions = goal },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = if (weeklyGoalSessions == goal) {
                                            "$goal ✓"
                                        } else {
                                            goal.toString()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("Stats", style = MaterialTheme.typography.titleMedium)
                        Text("Created challenges: $createdChallengesCount")
                        Text("Saved packs: $savedPacksCount")
                        Text("Weekly goal: $weeklyGoalSessions sessions")
                        Text("Training level: ${trainingIdentity.level}")
                        Text("Training title: ${trainingIdentity.title}")
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        onSave(
                            UserProfile(
                                displayName = displayName.trim().ifEmpty { "You" },
                                username = username.trim().ifEmpty { "neura_user" },
                                bio = bio.trim(),
                                favoriteTag = favoriteTag.trim(),
                                weeklyGoalSessions = weeklyGoalSessions.coerceIn(1, 7)
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save profile")
                }
            }

        }
    }
}

@Composable
private fun TrainingIdentityCard(
    identity: TrainingIdentity,
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Training Identity",
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
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "Level ${identity.level}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
                ) {
                    Text(
                        text = "LV ${identity.level}",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrainingIdentityMetric(
                    label = "Sessions",
                    value = identity.totalSessions.toString(),
                    modifier = Modifier.weight(1f)
                )

                TrainingIdentityMetric(
                    label = "Average",
                    value = identity.averageAccuracyText,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrainingIdentityMetric(
                    label = "Main style",
                    value = identity.mainStyleText,
                    modifier = Modifier.weight(1f)
                )

                TrainingIdentityMetric(
                    label = "Best area",
                    value = identity.bestAreaText,
                    modifier = Modifier.weight(1f)
                )
            }

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
                        text = "Weekly rhythm",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = identity.weeklyProgressText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = identity.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Button(
                onClick = onOpenShareIdentity,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Share identity")
            }
        }
    }
}

@Composable
private fun TrainingIdentityMetric(
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