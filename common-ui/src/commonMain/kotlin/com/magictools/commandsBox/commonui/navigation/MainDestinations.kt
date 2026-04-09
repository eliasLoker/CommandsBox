package com.magictools.commandsBox.commonui.navigation

enum class MainDestinations(
    val destinationKey: String
) {
    SPLASH("splash"),
    HOME("home");

    fun getArg() = "${destinationKey}_arg"
    fun getRoute() = "$destinationKey/{${destinationKey}_arg}"
    fun getRouteWithArg(argument: String) = "${destinationKey}/$argument"
}