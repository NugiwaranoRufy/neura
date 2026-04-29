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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.viewmodel.RoomDebugViewModel
import com.app.neura.ui.component.TopBackHeader

@Composable
fun RoomDebugScreen(
    viewModel: RoomDebugViewModel,
    onBack: () -> Unit
) {
    val challenges by viewModel.challenges.collectAsState()

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
                    title = "Room debug",
                    subtitle = "Inspect local database state.",
                    onBack = onBack
                )
            }

            item {
                Button(
                    onClick = { viewModel.insertDebugChallenge() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Insert debug challenge in Room")
                }
            }

            item {
                OutlinedButton(
                    onClick = { viewModel.refresh() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Refresh Room data")
                }
            }

            item {
                Text(
                    text = "Stored challenges: ${challenges.size}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

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
                            text = "Author: ${challenge.authorName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Tags: ${challenge.tags.joinToString()}",
                            style = MaterialTheme.typography.bodySmall
                        )

                        OutlinedButton(
                            onClick = { viewModel.deleteChallenge(challenge.id) },
                            modifier = Modifier.padding(top = 12.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }

        }
    }
}