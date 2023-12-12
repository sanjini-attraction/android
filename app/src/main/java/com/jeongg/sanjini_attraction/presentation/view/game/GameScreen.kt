package com.jeongg.sanjini_attraction.presentation.view.game

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.presentation.component.SanjiniTitle
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.ui.theme.main_yellow
import com.jeongg.sanjini_attraction.ui.theme.typography

const val FINISH_MESSAGE = "-24"

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel(),
){
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.messages.contains(FINISH_MESSAGE)){
        navController.navigate(Screen.FinishGameScreen.route)
    }
    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(context,message, Toast.LENGTH_LONG).show()
        }
    }
    SanjiniTitle(title = viewModel.title.value, description = viewModel.description.value) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
            Text(text = "게임 중", style = typography.headlineLarge, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier
) {
    val circleColors: List<Color> = listOf(
        main_yellow,
        Color.White
    )
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    CircularProgressIndicator(
        modifier = modifier
            .size(280.dp)
            .rotate(degrees = rotateAnimation)
            .border(
                width = 20.dp,
                brush = Brush.sweepGradient(circleColors),
                shape = CircleShape
            ),
        progress = 1f,
        strokeWidth = 0.dp,
    )
}