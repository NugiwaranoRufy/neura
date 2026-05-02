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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.data.model.MissionBadgesSummary
import com.app.neura.data.model.TrainingPlanSummary
import com.app.neura.data.model.TrainingRecordsSummary
import com.app.neura.data.model.WeeklyMissionsSummary

@Composable
fun MissionsHubScreen(
    weeklyMissionsSummary: WeeklyMissionsSummary,
    missionBadgesSummary: MissionBadgesSummary,
    trainingPlanSummary: TrainingPlanSummary,
    trainingRecordsSummary: TrainingRecordsSummary,
    onOpenMissionsList: () -> Unit,
    onOpenBadges: () -> Unit,
    onOpenTrainingPlan: () -> Unit,
    onOpenRecords: () -> Unit
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Missions",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Your goals, badges and records.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                HubCard(
                    title = "Weekly missions",
                    message = weeklyMissionsSummary.progressText,
                    buttonText = "Open missions",
                    onClick = onOpenMissionsList
                )
            }

            item {
                HubCard(
                    title = "Training plan",
                    message = trainingPlanSummary.message,
                    buttonText = "Open plan",
                    onClick = onOpenTrainingPlan
                )
            }

            item {
                HubCard(
                    title = "Badges",
                    message = missionBadgesSummary.progressText,
                    buttonText = "View badges",
                    onClick = onOpenBadges
                )
            }

            item {
                HubCard(
                    title = "Records",
                    message = trainingRecordsSummary.highlightMessage,
                    buttonText = "View records",
                    onClick = onOpenRecords
                )
            }
        }
    }
}

@Composable
private fun HubCard(
    title: String,
    message: String,
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
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}