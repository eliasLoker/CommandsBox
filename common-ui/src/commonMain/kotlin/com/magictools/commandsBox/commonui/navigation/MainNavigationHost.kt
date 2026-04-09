package com.magictools.commandsBox.commonui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.magictools.commandsBox.commonui.ui.home.HomeScreen
import com.magictools.commandsBox.commonui.ui.splash.SplashScreen

@Composable
fun MainNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.SPLASH.destinationKey
    ) {
        composable(
            route = MainDestinations.SPLASH.destinationKey
        ) {
            SplashScreen(
                onTimeOut = {
                    navController.navigate(MainDestinations.HOME.destinationKey) {
                        popUpTo(MainDestinations.SPLASH.destinationKey) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = MainDestinations.HOME.destinationKey
        ) {
            HomeScreen()
        }
    }
}