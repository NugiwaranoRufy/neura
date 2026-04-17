package com.app.neura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.neura.ui.screen.ChallengeScreen
import com.app.neura.ui.theme.NeuraTheme
import com.app.neura.viewmodel.ChallengeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NeuraTheme {
                val challengeViewModel: ChallengeViewModel = viewModel()
                ChallengeScreen(viewModel = challengeViewModel)
            }
        }
    }
}