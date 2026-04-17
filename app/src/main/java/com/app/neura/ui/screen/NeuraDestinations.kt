package com.app.neura.ui.screen

sealed class NeuraDestinations(val route: String) {
    data object Home : NeuraDestinations("home")
    data object Challenge : NeuraDestinations("challenge")
    data object Result : NeuraDestinations("result")
    data object Create : NeuraDestinations("create")
    data object MyChallenges : NeuraDestinations("my_challenges")
}