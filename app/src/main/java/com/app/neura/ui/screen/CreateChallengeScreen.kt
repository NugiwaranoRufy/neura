package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengeType
import com.app.neura.data.model.CreateChallengeForm
import com.app.neura.viewmodel.ChallengeViewModel
import com.app.neura.data.model.ChallengeDifficulty
import com.app.neura.data.model.TagCatalog
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.mutableStateListOf

@Composable
fun CreateChallengeScreen(
    viewModel: ChallengeViewModel,
    defaultAuthorName: String,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    var question by remember { mutableStateOf("") }
    var option1 by remember { mutableStateOf("") }
    var option2 by remember { mutableStateOf("") }
    var option3 by remember { mutableStateOf("") }
    var option4 by remember { mutableStateOf("") }
    var explanation by remember { mutableStateOf("") }
    var correctIndex by remember { mutableIntStateOf(0) }
    var type by remember { mutableStateOf(ChallengeType.LOGIC) }
    var errorText by remember { mutableStateOf<String?>(null) }
    var difficulty by remember { mutableStateOf(ChallengeDifficulty.EASY) }
    var authorName by remember { mutableStateOf(defaultAuthorName) }
    val selectedTags = remember { mutableStateListOf<String>() }
    val optionLabels = listOf("Option 1", "Option 2", "Option 3", "Option 4")

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        bottomBar = {
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    if (errorText != null) {
                        Text(
                            text = errorText!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.padding(top = 8.dp))
                    }

                    Button(
                        onClick = {
                            val success = viewModel.createChallenge(
                                CreateChallengeForm(
                                    question = question,
                                    option1 = option1,
                                    option2 = option2,
                                    option3 = option3,
                                    option4 = option4,
                                    correctIndex = correctIndex,
                                    explanation = explanation,
                                    type = type,
                                    difficulty = difficulty,
                                    authorName = authorName,
                                    tags = selectedTags.toList()
                                )
                            )

                            if (success) {
                                onSaved()
                            } else {
                                errorText = "Fill in all fields before saving."
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Save challenge")
                    }

                    Spacer(modifier = Modifier.padding(top = 8.dp))

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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 24.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Create challenge",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Author name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = option1,
                    onValueChange = { option1 = it },
                    label = { Text("Option 1") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = option2,
                    onValueChange = { option2 = it },
                    label = { Text("Option 2") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = option3,
                    onValueChange = { option3 = it },
                    label = { Text("Option 3") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = option4,
                    onValueChange = { option4 = it },
                    label = { Text("Option 4") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Correct answer",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(optionLabels.indices.toList()) { index ->
                OutlinedButton(
                    onClick = { correctIndex = index },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (correctIndex == index) {
                            "• ${optionLabels[index]}"
                        } else {
                            optionLabels[index]
                        }
                    )
                }
            }

            item {
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                OutlinedButton(
                    onClick = { type = ChallengeType.LOGIC },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (type == ChallengeType.LOGIC) "• Logic" else "Logic")
                }
            }

            item {
                OutlinedButton(
                    onClick = { type = ChallengeType.LATERAL },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (type == ChallengeType.LATERAL) "• Lateral" else "Lateral")
                }
            }

            item {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedButton(
                    onClick = { difficulty = ChallengeDifficulty.EASY },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (difficulty == ChallengeDifficulty.EASY) "• Easy" else "Easy")
                }

                OutlinedButton(
                    onClick = { difficulty = ChallengeDifficulty.MEDIUM },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (difficulty == ChallengeDifficulty.MEDIUM) "• Medium" else "Medium")
                }

                OutlinedButton(
                    onClick = { difficulty = ChallengeDifficulty.HARD },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (difficulty == ChallengeDifficulty.HARD) "• Hard" else "Hard")
                }

                Text(
                    text = "Tags",
                    style = MaterialTheme.typography.titleMedium
                )

                TagCatalog.challengeTags.forEach { tag ->
                    OutlinedButton(
                        onClick = {
                            if (selectedTags.contains(tag)) {
                                selectedTags.remove(tag)
                            } else {
                                selectedTags.add(tag)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            if (selectedTags.contains(tag)) "• $tag" else tag
                        )
                    }
                }

                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Explanation") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}