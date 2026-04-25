package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.ChallengePack
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip

@Composable
fun FeaturedPacksScreen(
    packs: List<ChallengePack>,
    onOpenPack: (ChallengePack) -> Unit,
    onBack: () -> Unit
) {
    val logicPacks = packs.filter { pack ->
        pack.tags.any { it.equals("logic", ignoreCase = true) }
    }

    val lateralPacks = packs.filter { pack ->
        pack.tags.any { it.equals("lateral", ignoreCase = true) }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                PackSection(
                    title = "🔥 Featured",
                    packs = packs,
                    onOpenPack = onOpenPack
                )
            }

            if (logicPacks.isNotEmpty()) {
                item {
                    PackSection(
                        title = "🧠 Logic Packs",
                        packs = logicPacks,
                        onOpenPack = onOpenPack
                    )
                }
            }

            if (lateralPacks.isNotEmpty()) {
                item {
                    PackSection(
                        title = "🎯 Lateral Packs",
                        packs = lateralPacks,
                        onOpenPack = onOpenPack
                    )
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
private fun PackSection(
    title: String,
    packs: List<ChallengePack>,
    onOpenPack: (ChallengePack) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = packs,
                key = { "${it.title}_${it.authorName}_${it.createdAt}" }
            ) { pack ->
                FeaturedPackCard(
                    pack = pack,
                    onClick = { onOpenPack(pack) }
                )
            }
        }
    }
}

@Composable
private fun FeaturedPackCard(
    pack: ChallengePack,
    onClick: () -> Unit
) {

    val averageDifficulty = if (pack.challenges.isEmpty()) {
        "N/A"
    } else {
        val average = pack.challenges.map { it.difficulty.ordinal }.average()
        when {
            average < 0.75 -> "Easy"
            average < 1.5 -> "Medium"
            else -> "Hard"
        }
    }

    val categoryBadge = when {
        pack.tags.any { it.equals("logic", ignoreCase = true) } -> "🧠 Logic"
        pack.tags.any { it.equals("lateral", ignoreCase = true) } -> "🎯 Lateral"
        else -> "★ Featured"
    }

    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = categoryBadge,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = pack.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            Text(
                text = pack.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )

            Text(
                text = "${pack.challenges.size} challenges",
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                text = "Difficulty: $averageDifficulty",
                style = MaterialTheme.typography.labelSmall
            )

            if (pack.tags.isNotEmpty()) {
                Text(
                    text = pack.tags.take(3).joinToString(" • "),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1
                )
            }
        }
    }
}