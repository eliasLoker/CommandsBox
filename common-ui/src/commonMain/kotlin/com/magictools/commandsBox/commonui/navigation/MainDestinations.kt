package com.magictools.commandsBox.commonui.navigation

import kotlinx.serialization.Serializable

sealed interface MainDestinations {

    @Serializable
    data object Splash : MainDestinations

    @Serializable
    data object Home : MainDestinations
}