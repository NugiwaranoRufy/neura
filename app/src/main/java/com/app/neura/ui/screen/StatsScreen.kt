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
import com.app.neura.data.model.GameSessionResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.app.neura.ui.component.SecondaryActionButton
import com.app.neura.ui.component.EmptyStateCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.app.neura.ui.component.TopBackHeader

@Composable
fun StatsScreen(
    sessions: List<GameSessionResult>,
    bestScoreText: String,
    averageScoreText: String,
    currentDailyStreak: Int,
    bestDailyStreak: Int,
    dailyCompletedToday: Boolean,
    onClearHistory: () -> Unit,
    onBack: () -> Unit
) {

    var showClearConfirmDialog by remember { mutableStateOf(false) }

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
                    title = "Stats",
                    subtitle = "Track your sessions and progress.",
                    onBack = onBack
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Sessions played: ${sessions.size}")
                        Text("Best score: $bestScoreText")
                        Text("Average score: $averageScoreText")
                        Text("Daily completed today: ${if (dailyCompletedToday) "Yes" else "No"}")
                        Text("Current daily streak: $currentDailyStreak")
                        Text("Best daily streak: $bestDailyStreak")
                    }
                }
            }

            item {
                Text(
                    text = "History",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (sessions.isEmpty()) {
                item {
                    EmptyStateCard(
                        icon = "📊",
                        title = "No stats yet",
                        message = "Complete a session or a Daily Challenge to start tracking your progress."
                    )
                }
            } else {
                items(sessions, key = { it.id }) { session ->
                    SessionResultCard(session)
                }

                item {
                    Button(
                        onClick = {
                            showClearConfirmDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Clear history")
                    }
                }
            }



        }

        if (showClearConfirmDialog) {
            AlertDialog(
                onDismissRequest = {
                    showClearConfirmDialog = false
                },
                title = {
                    Text("Clear stats history?")
                },
                text = {
                    Text("This will remove session history, but achievements will remain.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onClearHistory()
                            showClearConfirmDialog = false
                        }
                    ) {
                        Text("Clear history")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showClearConfirmDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

}

@Composable
private fun SessionResultCard(
    session: GameSessionResult
) {
    val dateText = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    ).format(Date(session.completedAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "${session.score}/${session.totalQuestions}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Source: ${session.source}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Type: ${session.type?.name ?: "Mixed"}",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}