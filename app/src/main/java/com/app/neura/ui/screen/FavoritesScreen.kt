package com.app.neura.ui.screen

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
import com.app.neura.data.model.ChallengePack
import com.app.neura.ui.component.EmptyStateCard
import com.app.neura.ui.component.TopBackHeader

@Composable
fun FavoritesScreen(
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
                TopBackHeader(
                    title = "Favorites",
                    subtitle = "Your saved packs and content.",
                    onBack = onBack
                )
            }

            if (packs.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = "⭐",
                        title = "No favorites yet",
                        message = "Mark packs or challenges as favorites to find them quickly here."
                    )
                }
            } else {
                items(packs, key = { it.localId }) { pack ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(pack.title, style = MaterialTheme.typography.titleMedium)
                            Text("Author: ${pack.authorName}", style = MaterialTheme.typography.bodySmall)

                            OutlinedButton(
                                onClick = { onOpenPack(pack.localId) },
                                modifier = Modifier.padding(top = 12.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Open")
                            }
                        }
                    }
                }
            }

        }
    }
}