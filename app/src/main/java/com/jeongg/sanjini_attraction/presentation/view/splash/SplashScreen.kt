package com.jeongg.sanjini_attraction.presentation.view.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.R
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.presentation.state.BluetoothUiState
import com.jeongg.sanjini_attraction.presentation.view.bluetooth.BluetoothViewModel
import com.jeongg.sanjini_attraction.ui.theme.main_yellow
import com.jeongg.sanjini_attraction.ui.theme.typography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: BluetoothViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true){
        delay(1500)
        val nextScreen = when {
            state.isConnected -> Screen.ReadyForGameScreen
            else -> Screen.BluetoothScreen
        }
        navController.popBackStack()
        navController.navigate(nextScreen.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.joystick),
            contentDescription = "splsah_character",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
        Text(
            text = "Sanjini Attraction",
            style = typography.titleLarge,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}

