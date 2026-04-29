package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.app.neura.data.model.ChallengeType
import com.app.neura.viewmodel.ChallengeViewModel
import com.app.neura.data.model.AccessibilitySettings
import com.app.neura.ui.component.EmptyStateCard
import com.app.neura.ui.component.SecondaryActionButton

@Composable
fun ChallengeScreen(
    viewModel: ChallengeViewModel,
    onSessionCompleted: () -> Unit,
    onExitSession: () -> Unit,
    accessibilitySettings: AccessibilitySettings,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    val challenge = uiState.currentChallenge
    if (challenge == null) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                EmptyStateCard(
                    icon = "🧠",
                    title = "No challenges available",
                    message = "Try importing a pack, creating a challenge, or changing your session filters."
                )

                androidx.compose.foundation.layout.Spacer(
                    modifier = Modifier.padding(top = 16.dp)
                )

                SecondaryActionButton(
                    text = "Back to Home",
                    onClick = onExitSession
                )
            }
        }
        return
        }
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = if (accessibilitySettings.readingHelper) {
                    Arrangement.spacedBy(8.dp)
                } else {
                    Arrangement.Top
                }
            ) {
                if (!accessibilitySettings.focusMode) {
                    Text(
                        text = if (accessibilitySettings.calmMode) "Neura" else "Neura",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = "${uiState.currentQuestionNumber} / ${uiState.totalQuestions}",
                    style = MaterialTheme.typography.labelMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (!accessibilitySettings.focusMode) {
                    Text(
                        text = when (challenge.type) {
                            ChallengeType.LOGIC -> "Logic"
                            ChallengeType.LATERAL -> "Lateral"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = challenge.question,
                            style = if (accessibilitySettings.readingHelper) {
                                MaterialTheme.typography.titleLarge
                            } else {
                                MaterialTheme.typography.titleMedium
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                challenge.options.forEachIndexed { index, option ->
                    val isSelected = uiState.selectedOptionIndex == index
                    val buttonText = if (isSelected) "• $option" else option

                    OutlinedButton(
                        onClick = {
                            if (!uiState.hasAnswered) {
                                viewModel.selectOption(index)
                            }
                        },
                        enabled = !uiState.hasAnswered,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        Text(text = buttonText)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (uiState.hasAnswered) {
                    Text(
                        text = if (uiState.isCorrect) "Correct" else "Not correct",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (uiState.isCorrect)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = challenge.explanation,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Score: ${uiState.score}",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val completed = viewModel.continueSession()
                            if (completed) {
                                onSessionCompleted()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            if (uiState.currentQuestionNumber >= uiState.totalQuestions) {
                                "See result"
                            } else {
                                "Continue"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedButton(
                    onClick = onExitSession,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        if (uiState.hasAnswered && uiState.currentQuestionNumber >= uiState.totalQuestions) {
                            "Back to Home"
                        } else {
                            "Exit session"
                        }
                    )
                }
            }
        }
    }
