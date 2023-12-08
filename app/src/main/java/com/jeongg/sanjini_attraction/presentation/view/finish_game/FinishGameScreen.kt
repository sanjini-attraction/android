package com.jeongg.sanjini_attraction.presentation.view.finish_game

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.R
import com.jeongg.sanjini_attraction.presentation.component.SanjiniButtonTitle
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.presentation.util.SanjiniEvent
import com.jeongg.sanjini_attraction.ui.theme.gray2
import com.jeongg.sanjini_attraction.ui.theme.gray3
import com.jeongg.sanjini_attraction.ui.theme.main_yellow
import com.jeongg.sanjini_attraction.ui.theme.typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FinishGameScreen(
    navController: NavController,
    viewModel: FinishGameViewModel = hiltViewModel()
){
    val context = LocalContext.current

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                is SanjiniEvent.FINISH -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                is SanjiniEvent.ERROR -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
    SanjiniButtonTitle(
        title = "게임 완료",
        description = "사용자가 게임에서 얻은 등수와 점수를 보여줍니다. 해당 게임에서 목표점수는 ${viewModel.goal.value}점이었습니다.",
        buttonText = "게임 다시 시작하기",
        onButtonClick = {
            viewModel.disconnect()
            navController.navigate(Screen.BluetoothScreen.route)
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(top = 10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            items(viewModel.result.value){ r ->
                GameResultElement(r.people, r.score, r.rank)
            }
        }
    }
}

@Composable
fun GameResultElement(index: Int, score: Int, rank: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, gray2), RoundedCornerShape(10.dp))
            .padding(start=10.dp, end = 20.dp, top = 15.dp, bottom = 15.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (rank <= 3){
            Image(
                painter = painterResource(R.drawable.rank),
                contentDescription = "rank_image",
                modifier = Modifier.size(30.dp)
            )
        }
        Row(
            modifier = Modifier.padding(start = 60.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${index}번",
                style = typography.titleMedium
            )
            Text(
                text = "(${rank}등)",
                style = typography.bodyMedium,
                color = gray3,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

        Text(
            text = "${score}점",
            style = typography.titleSmall,
            color = main_yellow,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}
