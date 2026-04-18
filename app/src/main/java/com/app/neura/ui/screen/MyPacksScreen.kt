package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun MyPacksScreen(
    packs: List<ChallengePack>,
    onOpenPack: (Long) -> Unit,
    onDeletePack: (Long) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(PackSortOption.NEWEST) }

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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "My packs",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search packs") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text("Sort by", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.padding(top = 8.dp))

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

            Spacer(modifier = Modifier.padding(top = 16.dp))

            if (filteredPacks.isEmpty()) {
                Text(
                    text = "No packs found.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.padding(top = 24.dp))

                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredPacks, key = { it.createdAt }) { pack ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = pack.title,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.padding(top = 8.dp))

                                Text(
                                    text = pack.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.padding(top = 8.dp))

                                Text(
                                    text = "Author: ${pack.authorName}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = "Challenges: ${pack.challenges.size}",
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Spacer(modifier = Modifier.padding(top = 16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { onOpenPack(pack.createdAt) },
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Text("Open")
                                    }

                                    Button(
                                        onClick = { onDeletePack(pack.createdAt) },
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(top = 12.dp))

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