package com.app.neura

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.app.neura.ui.screen.ChallengeScreen
import com.app.neura.ui.screen.HomeScreen
import com.app.neura.ui.screen.NeuraDestinations
import com.app.neura.ui.screen.ResultScreen
import com.app.neura.ui.theme.NeuraTheme
import com.app.neura.viewmodel.ChallengeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.app.neura.ui.screen.CreateChallengeScreen
import com.app.neura.ui.screen.MyChallengesScreen
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.app.neura.ui.screen.TransferChallengesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NeuraTheme {
                val navController = rememberNavController()
                val challengeViewModel: ChallengeViewModel = viewModel()
                val uiState by challengeViewModel.uiState.collectAsState()

                var importStatus by remember { mutableStateOf<String?>(null) }

                val exportLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.CreateDocument("application/json")
                ) { uri: Uri? ->
                    if (uri != null) {
                        val content = challengeViewModel.exportUserChallengesToJson()
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(content.toByteArray())
                        }
                        importStatus = "Export completed."
                    }
                }

                val importLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { uri: Uri? ->
                    if (uri != null) {
                        val content = contentResolver.openInputStream(uri)
                            ?.bufferedReader()
                            ?.use { it.readText() }

                        if (content != null) {
                            val success = challengeViewModel.importUserChallengesFromJson(content)
                            importStatus = if (success) {
                                "Import completed."
                            } else {
                                "Import failed. Invalid file."
                            }
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = NeuraDestinations.Home.route
                ) {
                    composable(NeuraDestinations.Home.route) {
                        HomeScreen(
                            onStartSession = { config ->
                                challengeViewModel.startSession(config)
                                navController.navigate(NeuraDestinations.Challenge.route)
                            },
                            onCreateChallenge = {
                                navController.navigate(NeuraDestinations.Create.route)
                            },
                            onOpenMyChallenges = {
                                navController.navigate(NeuraDestinations.MyChallenges.route)
                            },
                            onOpenTransfer = {
                                importStatus = null
                                navController.navigate(NeuraDestinations.Transfer.route)
                            },
                            userChallengeCount = challengeViewModel.getUserChallengeCount()
                        )
                    }

                    composable(NeuraDestinations.Challenge.route) {
                        ChallengeScreen(
                            viewModel = challengeViewModel,
                            onSessionCompleted = {
                                navController.navigate(NeuraDestinations.Result.route) {
                                    popUpTo(NeuraDestinations.Challenge.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable(NeuraDestinations.Result.route) {
                        ResultScreen(
                            score = uiState.score,
                            total = uiState.totalQuestions,
                            onPlayAgain = {
                                challengeViewModel.resetSession()
                                navController.navigate(NeuraDestinations.Home.route) {
                                    popUpTo(NeuraDestinations.Home.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable(NeuraDestinations.Create.route) {
                        CreateChallengeScreen(
                            viewModel = challengeViewModel,
                            onSaved = {
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(NeuraDestinations.MyChallenges.route) {
                        MyChallengesScreen(
                            challenges = challengeViewModel.getUserChallenges(),
                            onDeleteChallenge = { challengeId ->
                                challengeViewModel.deleteUserChallenge(challengeId)
                                navController.navigate(NeuraDestinations.MyChallenges.route) {
                                    popUpTo(NeuraDestinations.MyChallenges.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(NeuraDestinations.Transfer.route) {
                        TransferChallengesScreen(
                            userChallengeCount = challengeViewModel.getUserChallengeCount(),
                            importStatus = importStatus,
                            onExport = {
                                exportLauncher.launch("neura_challenges.json")
                            },
                            onImport = {
                                importLauncher.launch(arrayOf("application/json"))
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}