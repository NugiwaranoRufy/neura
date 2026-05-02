package com.app.neura.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class HomeTile(
    val title: String,
    val subtitle: String,
    val icon: String,
    val onClick: () -> Unit
)

@Composable
fun HomeHeader(
    dailyCompletedToday: Boolean,
    currentDailyStreak: Int,
    calmMode: Boolean,
    readingHelper: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(if (readingHelper) 18.dp else 12.dp)
        ) {
            Text(
                text = if (calmMode) "Neura" else "🧠 Neura",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Train your mind. Create challenges. Build your collection.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.45f)
            ) {
                Text(
                    text = if (dailyCompletedToday) {
                        if (calmMode) {
                            "Daily completed • Streak $currentDailyStreak"
                        } else {
                            "✅ Daily completed • 🔥 Streak $currentDailyStreak"
                        }
                    } else {
                        if (calmMode) {
                            "Daily not completed • Streak $currentDailyStreak"
                        } else {
                            "☀️ Daily waiting • 🔥 Streak $currentDailyStreak"
                        }
                    },
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun HomeTileCard(
    tile: HomeTile,
    calmMode: Boolean,
    readingHelper: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(if (readingHelper) 172.dp else 156.dp)
            .clickable { tile.onClick() },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = tile.icon,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = if (calmMode) {
                        MaterialTheme.typography.labelLarge
                    } else {
                        MaterialTheme.typography.titleLarge
                    }
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = tile.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )

                Text(
                    text = tile.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}