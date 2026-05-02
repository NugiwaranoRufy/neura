package com.app.neura.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.MissionBadgesSummary
import com.app.neura.data.model.WeeklyMissionsSummary

@Composable
fun WeeklyMissionsCompactCard(
    summary: WeeklyMissionsSummary,
    calmMode: Boolean,
    onOpenMissions: () -> Unit
) {
    val progress = if (summary.totalCount == 0) {
        0f
    } else {
        summary.completedCount.toFloat() / summary.totalCount.toFloat()
    }

    CompactProgressCard(
        title = if (calmMode) "Weekly missions" else "✅ Weekly missions",
        message = summary.message,
        progressText = summary.progressText,
        progress = progress,
        buttonText = "Open",
        onClick = onOpenMissions
    )
}

@Composable
fun MissionBadgesCompactCard(
    summary: MissionBadgesSummary,
    calmMode: Boolean,
    onOpenBadges: () -> Unit
) {
    val progress = if (summary.totalCount == 0) {
        0f
    } else {
        summary.unlockedCount.toFloat() / summary.totalCount.toFloat()
    }

    CompactProgressCard(
        title = if (calmMode) "Badges" else "🏅 Badges",
        message = summary.message,
        progressText = summary.progressText,
        progress = progress,
        buttonText = "View",
        onClick = onOpenBadges
    )
}

@Composable
private fun CompactProgressCard(
    title: String,
    message: String,
    progressText: String,
    progress: Float,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = progressText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(buttonText)
                }
            }

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}