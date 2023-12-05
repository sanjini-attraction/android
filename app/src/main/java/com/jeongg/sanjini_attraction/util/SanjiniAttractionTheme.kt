package com.jeongg.sanjini_attraction.util

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.presentation.navigation.sanjiniAttractionGraph
import com.jeongg.sanjini_attraction.presentation.view.bluetooth.BluetoothViewModel
import com.jeongg.sanjini_attraction.ui.theme.Sanjini_attractionTheme

@Composable
fun SanjiniAttractionTheme() {
    Sanjini_attractionTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.SplashScreen.route,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ){
                sanjiniAttractionGraph(navController = navController)
            }
        }
    }
}