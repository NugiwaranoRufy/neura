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
                Text(
                    text = "Stats",
                    style = MaterialTheme.typography.headlineMedium
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
                    Text("No sessions yet.")
                }
            } else {
                items(sessions, key = { it.id }) { session ->
                    SessionResultCard(session)
                }

                item {
                    Button(
                        onClick = onClearHistory,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Clear history")
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