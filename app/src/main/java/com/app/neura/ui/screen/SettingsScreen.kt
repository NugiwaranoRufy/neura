package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.ui.component.SecondaryActionButton

@Composable
fun SettingsScreen(
    onOpenAccessibility: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenTransfer: () -> Unit,
    onOpenRoomDebug: () -> Unit,
    onBack: () -> Unit
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text = "Manage your profile, accessibility, progress and app tools.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                SecondaryActionButton(
                    text = "Accessibility",
                    onClick = onOpenAccessibility
                )
            }

            item {
                SecondaryActionButton(
                    text = "Profile",
                    onClick = onOpenProfile
                )
            }

            item {
                SecondaryActionButton(
                    text = "Stats",
                    onClick = onOpenStats
                )
            }

            item {
                SecondaryActionButton(
                    text = "Achievements",
                    onClick = onOpenAchievements
                )
            }

            item {
                SecondaryActionButton(
                    text = "Import / Export",
                    onClick = onOpenTransfer
                )
            }

            item {
                SecondaryActionButton(
                    text = "Room debug",
                    onClick = onOpenRoomDebug
                )
            }

            item {
                SecondaryActionButton(
                    text = "Back",
                    onClick = onBack
                )
            }
        }
    }
}