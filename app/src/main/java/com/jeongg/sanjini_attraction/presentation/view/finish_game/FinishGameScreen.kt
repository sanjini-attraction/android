package com.jeongg.sanjini_attraction.presentation.view.finish_game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.R
import com.jeongg.sanjini_attraction.presentation.component.SanjiniButtonTitle
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.presentation.view.game.FINISH_MESSAGE
import com.jeongg.sanjini_attraction.ui.theme.gray2
import com.jeongg.sanjini_attraction.ui.theme.main_yellow
import com.jeongg.sanjini_attraction.ui.theme.typography

@Composable
fun FinishGameScreen(
    navController: NavController,
    viewModel: FinishGameViewModel = hiltViewModel()
){
    SanjiniButtonTitle(
        title = "게임 완료",
        description = "",
        buttonText = "게임 다시 시작하기",
        onButtonClick = {
            viewModel.disconnect()
            navController.navigate(Screen.BluetoothScreen.route)
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            viewModel.result.value.forEachIndexed { index, result ->
                item { GameResultElement(result, index) }
            }
        }

    }

}

@Composable
fun GameResultElement(result: String, index: Int) {
    if (result == FINISH_MESSAGE) return
    val splitResult = result.split(' ')
    val rank  = if (splitResult.size == 2) splitResult[0] else "???"
    val score = if (splitResult.size == 2) splitResult[1] else "???"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, gray2), RoundedCornerShape(10.dp))
            .padding(start=10.dp, end = 20.dp, top = 15.dp, bottom = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (index <= 2){
            Image(
                painter = painterResource(R.drawable.rank),
                contentDescription = "rank_image",
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = "${rank}번",
            style = typography.titleMedium,
            modifier = Modifier.padding(start = 60.dp)
        )
        Text(
            text = "${score}점",
            style = typography.titleSmall,
            color = main_yellow,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
