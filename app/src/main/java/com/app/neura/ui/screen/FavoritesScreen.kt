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
import androidx.compose.material3.Button
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
import com.app.neura.ui.component.EmptyStateCard
import com.app.neura.ui.component.TopBackHeader

@Composable
fun FavoritesScreen(
    packs: List<ChallengePack>,
    challenges: List<Challenge>,
    onOpenPack: (Long) -> Unit,
    onPlayChallenge: (Challenge) -> Unit,
    onBack: () -> Unit
) {
    val hasFavorites = packs.isNotEmpty() || challenges.isNotEmpty()

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
                    title = "Favorites",
                    subtitle = "Your saved packs and challenges.",
                    onBack = onBack
                )
            }

            if (!hasFavorites) {
                item {
                    EmptyStateCard(
                        icon = "⭐",
                        title = "No favorites yet",
                        message = "Mark packs or challenges as favorites to find them quickly here."
                    )
                }
            }

            if (packs.isNotEmpty()) {
                item {
                    Text(
                        text = "Favorite packs",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(
                    items = packs,
                    key = { it.localId },
                    contentType = { "favorite_pack" }
                ) { pack ->
                    FavoritePackCard(
                        pack = pack,
                        onOpenPack = {
                            onOpenPack(pack.localId)
                        }
                    )
                }
            }

            if (challenges.isNotEmpty()) {
                item {
                    Text(
                        text = "Favorite challenges",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(
                    items = challenges,
                    key = { it.id },
                    contentType = { "favorite_challenge" }
                ) { challenge ->
                    FavoriteChallengeCard(
                        challenge = challenge,
                        onPlayChallenge = {
                            onPlayChallenge(challenge)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritePackCard(
    pack: ChallengePack,
    onOpenPack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "📦 Pack",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = pack.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = pack.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${pack.challenges.size} challenges",
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedButton(
                onClick = onOpenPack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Open pack")
            }
        }
    }
}

@Composable
private fun FavoriteChallengeCard(
    challenge: Challenge,
    onPlayChallenge: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🧩 Challenge",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = challenge.question,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Author: ${challenge.authorName}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Difficulty: ${challenge.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.bodySmall
            )

            Button(
                onClick = onPlayChallenge,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Play challenge")
            }
        }
    }
}