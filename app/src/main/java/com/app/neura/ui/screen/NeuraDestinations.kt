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
    data object PackDetails : NeuraDestinations("pack_details/{localId}") {
        fun createRoute(localId: Long) = "pack_details/$localId"
    }

    data object Discover : NeuraDestinations("discover")
    data object FeaturedPackDetails : NeuraDestinations("featured_pack_details/{createdAt}") {
        fun createRoute(createdAt: Long) = "featured_pack_details/$createdAt"
    }

    data object Favorites : NeuraDestinations("favorites")
    data object PlayLater : NeuraDestinations("play_later")
    data object Profile : NeuraDestinations("profile")
    data object AuthorDetails : NeuraDestinations("author_details/{authorName}") {
        fun createRoute(authorName: String) = "author_details/$authorName"
    }
}