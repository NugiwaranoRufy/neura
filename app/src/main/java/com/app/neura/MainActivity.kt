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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.app.neura.ui.screen.EditChallengeScreen
import com.app.neura.data.model.ChallengePack
import com.app.neura.ui.screen.ExportPackScreen
import com.app.neura.ui.screen.ImportPackPreviewScreen
import androidx.navigation.navArgument
import com.app.neura.ui.screen.MyPacksScreen
import com.app.neura.ui.screen.PackDetailsScreen
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val incomingUri: Uri? = if (intent?.action == Intent.ACTION_VIEW) {
            intent.data
        } else {
            null
        }

        setContent {
            NeuraTheme {
                val navController = rememberNavController()
                val challengeViewModel: ChallengeViewModel = viewModel()
                val uiState by challengeViewModel.uiState.collectAsState()

                var importStatus by remember { mutableStateOf<String?>(null) }

                var importedPackPreview by remember { mutableStateOf<ChallengePack?>(null) }

                var pendingPackExportContent by remember { mutableStateOf<String?>(null) }

                var initialIncomingPackHandled by remember { mutableStateOf(false) }

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

                val importPackLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { uri: Uri? ->
                    if (uri != null) {
                        val content = contentResolver.openInputStream(uri)
                            ?.bufferedReader()
                            ?.use { it.readText() }

                        if (content != null) {
                            val preview = challengeViewModel.previewChallengePack(content)
                            if (preview != null) {
                                importedPackPreview = preview
                                navController.navigate(NeuraDestinations.ImportPackPreview.route)
                            } else {
                                importStatus = "Invalid pack file."
                            }
                        }
                    }
                }

                val packExportLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.CreateDocument("application/json")
                ) { uri: Uri? ->
                    if (uri != null && pendingPackExportContent != null) {
                        contentResolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(pendingPackExportContent!!.toByteArray())
                        }
                        importStatus = "Pack export completed."
                        pendingPackExportContent = null
                    }
                }

                LaunchedEffect(incomingUri, initialIncomingPackHandled) {
                    if (!initialIncomingPackHandled && incomingUri != null) {
                        val content = contentResolver.openInputStream(incomingUri)
                            ?.bufferedReader()
                            ?.use { it.readText() }

                        if (content != null) {
                            val preview = challengeViewModel.previewChallengePack(content)
                            if (preview != null) {
                                importedPackPreview = preview
                                importStatus = null
                                initialIncomingPackHandled = true
                                navController.navigate(NeuraDestinations.ImportPackPreview.route)
                            } else {
                                importStatus = "Invalid pack file."
                                initialIncomingPackHandled = true
                            }
                        } else {
                            initialIncomingPackHandled = true
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
                            onOpenMyPacks = {
                                navController.navigate(NeuraDestinations.MyPacks.route)
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
                            onEditChallenge = { challengeId ->
                                navController.navigate(NeuraDestinations.EditChallenge.createRoute(challengeId))
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
                            onExportPack = {
                                navController.navigate(NeuraDestinations.ExportPack.route)
                            },
                            onImportPack = {
                                importPackLauncher.launch(arrayOf("application/json"))
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = NeuraDestinations.EditChallenge.route,
                        arguments = listOf(
                            navArgument("challengeId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val challengeId = backStackEntry.arguments?.getInt("challengeId") ?: return@composable
                        val challenge = challengeViewModel.getUserChallengeById(challengeId) ?: return@composable

                        EditChallengeScreen(
                            challenge = challenge,
                            viewModel = challengeViewModel,
                            onSaved = {
                                navController.popBackStack()
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(NeuraDestinations.ExportPack.route) {
                        ExportPackScreen(
                            challenges = challengeViewModel.getUserChallenges(),
                            onExport = { title, description, authorName, challengeIds ->
                                val json = challengeViewModel.exportChallengePackToJson(
                                    title = title,
                                    description = description,
                                    authorName = authorName,
                                    challengeIds = challengeIds
                                )

                                if (json != null) {
                                    pendingPackExportContent = json
                                    packExportLauncher.launch("neura_pack.json")
                                }
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(NeuraDestinations.ImportPackPreview.route) {
                        val pack = importedPackPreview
                        if (pack != null) {
                            ImportPackPreviewScreen(
                                pack = pack,
                                onImport = {
                                    val success = challengeViewModel.importChallengePack(pack)
                                    importStatus = if (success) {
                                        "Pack import completed."
                                    } else {
                                        "Pack import failed."
                                    }
                                    importedPackPreview = null
                                    navController.navigate(NeuraDestinations.Transfer.route) {
                                        popUpTo(NeuraDestinations.Transfer.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onBack = {
                                    importedPackPreview = null
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                    composable(NeuraDestinations.MyPacks.route) {
                        MyPacksScreen(
                            packs = challengeViewModel.getPacks(),
                            onOpenPack = { createdAt ->
                                navController.navigate(NeuraDestinations.PackDetails.createRoute(createdAt))
                            },
                            onDeletePack = { createdAt ->
                                challengeViewModel.deletePack(createdAt)
                                navController.navigate(NeuraDestinations.MyPacks.route) {
                                    popUpTo(NeuraDestinations.MyPacks.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = NeuraDestinations.PackDetails.route,
                        arguments = listOf(
                            navArgument("createdAt") { type = NavType.LongType }
                        )
                    ) { backStackEntry ->
                        val createdAt = backStackEntry.arguments?.getLong("createdAt") ?: return@composable
                        val pack = challengeViewModel.getPackByCreatedAt(createdAt) ?: return@composable

                        PackDetailsScreen(
                            pack = pack,
                            onPlayPack = {
                                challengeViewModel.startSessionFromPack(pack)
                                navController.navigate(NeuraDestinations.Challenge.route)
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