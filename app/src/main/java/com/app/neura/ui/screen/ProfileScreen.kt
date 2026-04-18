package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.OutlinedButton
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

@Composable
fun ProfileScreen(
    profile: UserProfile,
    createdChallengesCount: Int,
    savedPacksCount: Int,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var displayName by remember(profile.displayName) { mutableStateOf(profile.displayName) }
    var username by remember(profile.username) { mutableStateOf(profile.username) }
    var bio by remember(profile.bio) { mutableStateOf(profile.bio) }
    var favoriteTag by remember(profile.favoriteTag) { mutableStateOf(profile.favoriteTag) }

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
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineMedium
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text("Stats", style = MaterialTheme.typography.titleMedium)
                        Text("Created challenges: $createdChallengesCount")
                        Text("Saved packs: $savedPacksCount")
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
                                favoriteTag = favoriteTag.trim()
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Save profile")
                }
            }

            item {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back")
                }
            }
        }
    }
}