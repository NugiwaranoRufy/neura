package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.Challenge
import com.app.neura.data.model.TagCatalog
import androidx.compose.runtime.mutableStateListOf

@Composable
fun ExportPackScreen(
    challenges: List<Challenge>,
    defaultAuthorName: String,
    onExport: (
        title: String,
        description: String,
        authorName: String,
        challengeIds: List<Int>,
        tags: List<String>
    ) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf(defaultAuthorName) }
    var errorText by remember { mutableStateOf<String?>(null) }

    val selectedIds = remember { mutableStateListOf<Int>() }
    val selectedPackTags = remember { mutableStateListOf<String>() }

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
                    }

                    Button(
                        onClick = {
                            if (
                                title.isBlank() ||
                                description.isBlank() ||
                                authorName.isBlank() ||
                                selectedIds.isEmpty()
                            ) {
                                errorText = "Fill all fields and select at least one challenge."
                            } else {
                                onExport(title, description, authorName, selectedIds.toList(), selectedPackTags.toList())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Export pack")
                    }

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
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
                    text = "Export pack",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Pack title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Author name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Pack tags",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(TagCatalog.packTags, key = { it }) { tag ->
                OutlinedButton(
                    onClick = {
                        if (selectedPackTags.contains(tag)) {
                            selectedPackTags.remove(tag)
                        } else {
                            selectedPackTags.add(tag)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (selectedPackTags.contains(tag)) "• $tag" else tag)
                }
            }

            item {
                Text(
                    text = "Select challenges",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(challenges, key = { it.id }) { challenge ->
                Surface(
                    tonalElevation = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = selectedIds.contains(challenge.id),
                                onCheckedChange = { checked ->
                                    if (checked) {
                                        if (!selectedIds.contains(challenge.id)) {
                                            selectedIds.add(challenge.id)
                                        }
                                    } else {
                                        selectedIds.remove(challenge.id)
                                    }
                                }
                            )

                            Column(
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(
                                    text = challenge.question,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text(
                                    text = "Difficulty: ${challenge.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}