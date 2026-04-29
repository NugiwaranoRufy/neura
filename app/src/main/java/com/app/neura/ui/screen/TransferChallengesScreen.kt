package com.app.neura.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.neura.ui.component.InfoStateCard
import com.app.neura.ui.component.TopBackHeader

@Composable
fun TransferChallengesScreen(
    userChallengeCount: Int,
    importStatus: String?,
    onExport: () -> Unit,
    onImport: () -> Unit,
    onExportPack: () -> Unit,
    onImportPack: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
                TopBackHeader(
                    title = "Import / Export",
                    subtitle = "Move your challenges and packs safely.",
                    onBack = onBack
                )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your created challenges: $userChallengeCount",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onExport,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Export my challenges")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onImport,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Import challenges")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onExportPack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Export pack")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onImportPack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Import pack")
            }

            if (importStatus != null) {
                Spacer(modifier = Modifier.height(20.dp))
                    InfoStateCard(
                        message = importStatus
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
