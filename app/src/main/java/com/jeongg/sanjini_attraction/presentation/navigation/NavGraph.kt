package com.jeongg.sanjini_attraction.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jeongg.sanjini_attraction.presentation.view.bluetooth.BluetoothScreen
import com.jeongg.sanjini_attraction.presentation.view.ready_game.ReadyForGameScreen
import com.jeongg.sanjini_attraction.presentation.view.splash.SplashScreen

fun NavGraphBuilder.sanjiniAttractionGraph(
    navController: NavController
){
    composable(route = Screen.SplashScreen.route){ SplashScreen(navController) }
    composable(route = Screen.BluetoothScreen.route) { BluetoothScreen(navController) }
    composable(route = Screen.ReadyForGameScreen.route) { ReadyForGameScreen(navController) }

}