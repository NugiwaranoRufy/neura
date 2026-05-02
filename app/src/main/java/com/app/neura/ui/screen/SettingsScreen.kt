package com.app.neura.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.ui.component.SecondaryActionButton
import com.app.neura.ui.component.TopBackHeader


private data class SettingsTile(
    val title: String,
    val subtitle: String,
    val icon: String,
    val onClick: () -> Unit
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onOpenAccessibility: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenActivity: () -> Unit,
    onOpenShareIdentity: () -> Unit,
    onOpenRecords: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenTransfer: () -> Unit,
    onOpenRoomDebug: () -> Unit,
    onBack: () -> Unit
) {
    val tiles = listOf(
        SettingsTile("Accessibility", "Theme, text, contrast", "♿", onOpenAccessibility),
        SettingsTile("Profile", "Author identity", "👤", onOpenProfile),
        SettingsTile("Activity", "Review your recent training timeline.", icon = "📰", onOpenActivity),
        SettingsTile("Share identity", "Preview and share your Training Identity.", icon = "🪪", onOpenShareIdentity),
        SettingsTile("Records", "Review your personal bests.", icon = "🥇", onOpenRecords),
        SettingsTile("Stats", "Session history", "📊", onOpenStats),
        SettingsTile("Achievements", "Milestones", "🏆", onOpenAchievements),
        SettingsTile("Import / Export", "Share content", "🔁", onOpenTransfer),
        SettingsTile("Room debug", "Database tools", "🛠️", onOpenRoomDebug)
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            item {
                TopBackHeader(
                    title = "Settings",
                    subtitle = "Manage your settings.",
                    onBack = onBack
                )
            }

            item {
                Text(
                    text = "Manage your profile, accessibility, progress and app tools.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = 2
                ) {
                    tiles.forEach { tile ->
                        SettingsTileCard(
                            tile = tile,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (tiles.size % 2 != 0) {
                        androidx.compose.foundation.layout.Spacer(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTileCard(
    tile: SettingsTile,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(144.dp)
            .clickable { tile.onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = tile.icon,
                style = MaterialTheme.typography.titleLarge
            )

            androidx.compose.foundation.layout.Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = tile.title,
                    style = MaterialTheme.typography.titleMedium
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