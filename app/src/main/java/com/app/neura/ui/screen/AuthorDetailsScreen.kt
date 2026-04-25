package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengePack
import com.app.neura.data.model.UserProfile
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.layout.Column

@Composable
fun AuthorDetailsScreen(
    authorName: String,
    profile: UserProfile?,
    challenges: List<Challenge>,
    packs: List<ChallengePack>,
    onOpenPack: (Long) -> Unit,
    onBack: () -> Unit
) {
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "👤 ${authorName.take(1).uppercase()}",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Text(
                            text = "${challenges.size} challenges • ${packs.size} packs",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = authorName,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            if (profile != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "@${profile.username}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (profile.bio.isNotBlank()) {
                                Text(
                                    text = profile.bio,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            if (profile.favoriteTag.isNotBlank()) {
                                Text(
                                    text = "Favorite tag: ${profile.favoriteTag}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Packs by this author: ${packs.size}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (packs.isEmpty()) {
                item {
                    Text(
                        text = "No packs found.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(packs, key = { "${it.localId}_${it.createdAt}_${it.title}" }) { pack ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = pack.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = pack.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = "Challenges: ${pack.challenges.size}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            if (pack.tags.isNotEmpty()) {
                                Text(
                                    text = "Tags: ${pack.tags.joinToString()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            OutlinedButton(
                                onClick = { onOpenPack(pack.localId) },
                                modifier = Modifier.padding(top = 12.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Open pack")
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Challenges by this author: ${challenges.size}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (challenges.isEmpty()) {
                item {
                    Text(
                        text = "No challenges found.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(challenges, key = { it.id }) { challenge ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = challenge.question,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "Type: ${challenge.type.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = "Difficulty: ${challenge.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            if (challenge.tags.isNotEmpty()) {
                                Text(
                                    text = "Tags: ${challenge.tags.joinToString()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
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