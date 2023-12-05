package com.jeongg.sanjini_attraction.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jeongg.sanjini_attraction.presentation.view.bluetooth.BluetoothScreen
import com.jeongg.sanjini_attraction.presentation.view.finish_game.FinishGameScreen
import com.jeongg.sanjini_attraction.presentation.view.game.GameScreen
import com.jeongg.sanjini_attraction.presentation.view.ready_game.ReadyForGameScreen
import com.jeongg.sanjini_attraction.presentation.view.splash.SplashScreen

fun NavGraphBuilder.sanjiniAttractionGraph(
    navController: NavController
){
    composable(route = Screen.SplashScreen.route){
        SplashScreen(navController)
    }
    composable(route = Screen.BluetoothScreen.route) {
        BluetoothScreen(navController)
    }
    composable(route = Screen.ReadyForGameScreen.route) {
        ReadyForGameScreen(navController)
    }
    composable(
        route = Screen.GameScreen.route + "?index={index}",
        arguments = listOf(
            navArgument("index"){
                type = NavType.IntType
                defaultValue = 0
            }
        )
    ){
        GameScreen(navController)
    }
    composable(route = Screen.FinishGameScreen.route){FinishGameScreen(navController)}
}