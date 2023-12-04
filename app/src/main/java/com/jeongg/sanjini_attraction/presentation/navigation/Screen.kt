package com.jeongg.sanjini_attraction.presentation.navigation

sealed class Screen (val route: String){
    object SplashScreen: Screen("splash_screen")
    object BluetoothScreen: Screen("bluetooth_screen")
    object ReadyForGameScreen: Screen("ready_for_game_screen")
}