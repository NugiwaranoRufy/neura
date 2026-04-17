package com.app.neura.ui.screen

sealed class NeuraDestinations(val route: String) {
    data object Home : NeuraDestinations("home")
    data object Challenge : NeuraDestinations("challenge")
    data object Result : NeuraDestinations("result")
    data object Create : NeuraDestinations("create")
    data object MyChallenges : NeuraDestinations("my_challenges")
    data object Transfer : NeuraDestinations("transfer")
    data object EditChallenge : NeuraDestinations("edit_challenge/{challengeId}") {
        fun createRoute(challengeId: Int) = "edit_challenge/$challengeId"
    }
    data object ExportPack : NeuraDestinations("export_pack")
    data object ImportPackPreview : NeuraDestinations("import_pack_preview")
    data object MyPacks : NeuraDestinations("my_packs")
    data object PackDetails : NeuraDestinations("pack_details/{createdAt}") {
        fun createRoute(createdAt: Long) = "pack_details/$createdAt"
    }
}