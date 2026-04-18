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
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.ChallengeType
import com.app.neura.ui.screen.filter.ChallengeTypeFilter
import com.app.neura.ui.screen.filter.DifficultyFilter

@Composable
fun MyChallengesScreen(
    challenges: List<Challenge>,
    onDeleteChallenge: (Int) -> Unit,
    onEditChallenge: (Int) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var typeFilter by remember { mutableStateOf(ChallengeTypeFilter.ALL) }
    var difficultyFilter by remember { mutableStateOf(DifficultyFilter.ALL) }

    val filteredChallenges = challenges.filter { challenge ->
        val matchesQuery =
            challenge.question.contains(query, ignoreCase = true) ||
                    challenge.authorName.contains(query, ignoreCase = true)

        val matchesType = when (typeFilter) {
            ChallengeTypeFilter.ALL -> true
            ChallengeTypeFilter.LOGIC -> challenge.type == ChallengeType.LOGIC
            ChallengeTypeFilter.LATERAL -> challenge.type == ChallengeType.LATERAL
        }

        val matchesDifficulty = when (difficultyFilter) {
            DifficultyFilter.ALL -> true
            DifficultyFilter.EASY -> challenge.difficulty == ChallengeDifficulty.EASY
            DifficultyFilter.MEDIUM -> challenge.difficulty == ChallengeDifficulty.MEDIUM
            DifficultyFilter.HARD -> challenge.difficulty == ChallengeDifficulty.HARD
        }

        matchesQuery && matchesType && matchesDifficulty
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
                text = "My challenges",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search challenges") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text("Type", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { typeFilter = ChallengeTypeFilter.ALL }) {
                    Text(if (typeFilter == ChallengeTypeFilter.ALL) "• All" else "All")
                }
                OutlinedButton(onClick = { typeFilter = ChallengeTypeFilter.LOGIC }) {
                    Text(if (typeFilter == ChallengeTypeFilter.LOGIC) "• Logic" else "Logic")
                }
                OutlinedButton(onClick = { typeFilter = ChallengeTypeFilter.LATERAL }) {
                    Text(if (typeFilter == ChallengeTypeFilter.LATERAL) "• Lateral" else "Lateral")
                }
            }

            Spacer(modifier = Modifier.padding(top = 12.dp))

            Text("Difficulty", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { difficultyFilter = DifficultyFilter.ALL }) {
                    Text(if (difficultyFilter == DifficultyFilter.ALL) "• All" else "All")
                }
                OutlinedButton(onClick = { difficultyFilter = DifficultyFilter.EASY }) {
                    Text(if (difficultyFilter == DifficultyFilter.EASY) "• Easy" else "Easy")
                }
                OutlinedButton(onClick = { difficultyFilter = DifficultyFilter.MEDIUM }) {
                    Text(if (difficultyFilter == DifficultyFilter.MEDIUM) "• Medium" else "Medium")
                }
                OutlinedButton(onClick = { difficultyFilter = DifficultyFilter.HARD }) {
                    Text(if (difficultyFilter == DifficultyFilter.HARD) "• Hard" else "Hard")
                }
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            if (filteredChallenges.isEmpty()) {
                Text(
                    text = "No challenges found.",
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
                    items(filteredChallenges, key = { it.id }) { challenge ->
                        ChallengeManageCard(
                            challenge = challenge,
                            onDelete = { onDeleteChallenge(challenge.id) },
                            onEdit = { onEditChallenge(challenge.id) }
                        )
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

@Composable
private fun ChallengeManageCard(
    challenge: Challenge,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = when (challenge.type) {
                    ChallengeType.LOGIC -> "Logic"
                    ChallengeType.LATERAL -> "Lateral"
                },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = challenge.question,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "Difficulty: ${challenge.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Author: ${challenge.authorName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Correct answer: ${challenge.options[challenge.correctIndex]}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Edit")
                }

                Button(
                    onClick = onDelete,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Delete")
                }
            }
        }
    }
}