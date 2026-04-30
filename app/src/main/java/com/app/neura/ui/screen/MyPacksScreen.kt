package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.app.neura.data.model.ChallengePack
import com.app.neura.ui.screen.filter.PackSortOption
import com.app.neura.ui.util.averageDifficultyText
import com.app.neura.ui.util.categoryBadgeText
import com.app.neura.ui.util.estimatedTimeText
import com.app.neura.ui.component.EmptyStateCard
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import com.app.neura.ui.component.TopBackHeader

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyPacksScreen(
    packs: List<ChallengePack>,
    favoritePackIds: Set<Long>,
    playLaterPackIds: Set<Long>,
    onOpenPack: (Long) -> Unit,
    onDeletePack: (ChallengePack) -> Unit,
    onRestorePack: (ChallengePack) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onTogglePlayLater: (Long) -> Unit,
    onOpenAuthor: (String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(PackSortOption.NEWEST) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val filteredPacks = packs
        .filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.authorName.contains(query, ignoreCase = true)
        }
        .let { list ->
            when (sortOption) {
                PackSortOption.NEWEST -> list.sortedByDescending { it.createdAt }
                PackSortOption.OLDEST -> list.sortedBy { it.createdAt }
                PackSortOption.TITLE -> list.sortedBy { it.title.lowercase() }
            }
        }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
    Surface(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {

        val isLoading = packs.isEmpty()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (isLoading) {
                items(3) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .height(120.dp)
                    ) {}
                }
            }

            item {
                TopBackHeader(
                    title = "My packs",
                    subtitle = "Manage your saved collections.",
                    onBack = onBack
                )
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search packs") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text("Sort by", style = MaterialTheme.typography.titleMedium)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { sortOption = PackSortOption.NEWEST }) {
                        Text(if (sortOption == PackSortOption.NEWEST) "• Newest" else "Newest")
                    }
                    OutlinedButton(onClick = { sortOption = PackSortOption.OLDEST }) {
                        Text(if (sortOption == PackSortOption.OLDEST) "• Oldest" else "Oldest")
                    }
                    OutlinedButton(onClick = { sortOption = PackSortOption.TITLE }) {
                        Text(if (sortOption == PackSortOption.TITLE) "• Title" else "Title")
                    }
                }
            }

            item {
                Text(
                    text = "Results: ${filteredPacks.size}",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            if (filteredPacks.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = "📦",
                        title = "No packs yet",
                        message = "Import a pack, discover featured content, or create your own collection."
                    )
                }
            } else {
                items(
                    items = filteredPacks,
                    key = { it.localId },
                    contentType = { "pack" }
                ) { pack ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "📦 Community pack",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = pack.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = pack.categoryBadgeText(),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = pack.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = "Author: ${pack.authorName}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clickable { onOpenAuthor(pack.authorName) },
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "by ${pack.authorName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Challenges: ${pack.challenges.size}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "Difficulty: ${pack.averageDifficultyText()}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Text(
                                text = "Time: ${pack.estimatedTimeText()}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            if (pack.tags.isNotEmpty()) {
                                Text(
                                    text = "Tags: ${pack.tags.joinToString()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                maxItemsInEachRow = 2
                            ) {
                                OutlinedButton(
                                    onClick = { onOpenPack(pack.localId) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Open")
                                }

                                Button(
                                    onClick = {
                                        onDeletePack(pack)

                                        coroutineScope.launch {
                                            val result = snackbarHostState.showSnackbar(
                                                message = "Pack deleted",
                                                actionLabel = "Undo"
                                            )

                                            if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                                                onRestorePack(pack)
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Delete")
                                }

                                OutlinedButton(
                                    onClick = { onToggleFavorite(pack.localId) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(if (favoritePackIds.contains(pack.localId)) "★ Fav" else "☆ Fav")
                                }

                                OutlinedButton(
                                    onClick = { onTogglePlayLater(pack.localId) },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(if (playLaterPackIds.contains(pack.localId)) "• Later" else "Later")
                                }
                            }
                        }
                    }
                }
            }

        }
    }
} }