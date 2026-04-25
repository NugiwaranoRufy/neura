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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.SessionAnswer

@Composable
fun SessionReviewScreen(
    answers: List<SessionAnswer>,
    onBackToResult: () -> Unit,
    onBackToHome: () -> Unit
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
                    text = "Review answers",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            if (answers.isEmpty()) {
                item {
                    Text(
                        text = "No answers available for this session.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                itemsIndexed(
                    items = answers,
                    key = { _, answer -> answer.challenge.id }
                ) { index, answer ->
                    ReviewAnswerCard(
                        number = index + 1,
                        answer = answer
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = onBackToResult,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back to result")
                }
            }

            item {
                OutlinedButton(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Back to Home")
                }
            }
        }
    }
}

@Composable
private fun ReviewAnswerCard(
    number: Int,
    answer: SessionAnswer
) {
    val challenge = answer.challenge

    val selectedText = answer.selectedOptionIndex
        ?.let { index -> challenge.options.getOrNull(index) }
        ?: "No answer"

    val correctText = challenge.options.getOrNull(challenge.correctIndex) ?: "Unknown"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Question $number",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = challenge.question,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (answer.isCorrect) "Result: Correct" else "Result: Not correct",
                style = MaterialTheme.typography.bodyMedium,
                color = if (answer.isCorrect) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )

            Text(
                text = "Your answer: $selectedText",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Correct answer: $correctText",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Explanation: ${challenge.explanation}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}