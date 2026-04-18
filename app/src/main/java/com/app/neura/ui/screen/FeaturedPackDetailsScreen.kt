package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengePack

@Composable
fun FeaturedPackDetailsScreen(
    pack: ChallengePack,
    onImportPack: () -> Unit,
    onOpenAuthor: () -> Unit,
    onBack: () -> Unit
) {
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
                text = pack.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = pack.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "Author: ${pack.authorName}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clickable { onOpenAuthor() },
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Challenges: ${pack.challenges.size}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Button(
                onClick = onImportPack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Import this pack")
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pack.challenges, key = { it.id }) { challenge ->
                    Surface(
                        tonalElevation = 1.dp,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = challenge.question,
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.padding(top = 6.dp))

                            Text(
                                text = "Type: ${challenge.type.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            if (pack.tags.isNotEmpty()) {
                                Text(
                                    text = "Tags: ${pack.tags.joinToString()}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Text(
                                text = "Difficulty: ${challenge.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                style = MaterialTheme.typography.bodySmall
                            )
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