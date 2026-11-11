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
        startDestination = MainDestinations.Splash
    ) {
        composable<MainDestinations.Splash> {
            SplashScreen(
                onTimeOut = {
                    navController.navigate(MainDestinations.Home) {
                        popUpTo(MainDestinations.Splash) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<MainDestinations.Home> {
            HomeScreen()
        }
    }
}