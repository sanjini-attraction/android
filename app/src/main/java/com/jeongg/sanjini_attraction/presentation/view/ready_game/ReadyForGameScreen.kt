package com.jeongg.sanjini_attraction.presentation.view.ready_game

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.R
import com.jeongg.sanjini_attraction.domain.model.SelectionOption
import com.jeongg.sanjini_attraction.presentation.component.SanjiniButtonTitle
import com.jeongg.sanjini_attraction.presentation.component.SanjiniDivider
import com.jeongg.sanjini_attraction.presentation.component.SanjiniTextField
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.presentation.util.SanjiniEvent
import com.jeongg.sanjini_attraction.presentation.util.addFocusCleaner
import com.jeongg.sanjini_attraction.ui.theme.main_yellow
import com.jeongg.sanjini_attraction.ui.theme.typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ReadyForGameScreen(
    navController: NavController,
    viewModel: ReadyForGameViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val eventFlow by viewModel.eventFlow.collectAsState()

    LaunchedEffect(key1 = eventFlow){
       when(eventFlow){
            is SanjiniEvent.SUCCESS -> {
                val index = viewModel.getSelectedIndex()
                navController.navigate(Screen.GameScreen.route + "?index=$index")
            }
            is SanjiniEvent.ERROR -> Toast.makeText(context, "데이터 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
            else -> {}
        }

    }
    SanjiniButtonTitle(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        buttonText = "게임 시작하기",
        onButtonClick = {viewModel.onEvent(ReadyForGameEvent.StartGame)},
        title = "게임 준비하기",
        description = "게임 종류, 인원, 목표 점수를 선택해서 게임을 시작해주세요. 목표 점수는 1~100까지만 선택할 수 있습니다.\n"
    ){
        LazyColumn {
            item {
                GameSelect(
                    viewModel.options,
                    viewModel::selectionOptionSelected
                )
            }
            item { SanjiniDivider(modifier = Modifier.padding(vertical = 20.dp)) }
            item {
                GameSettingSelect(
                    title = "참여 인원",
                    text = viewModel.people.value,
                    onValueChange = { viewModel.onEvent(ReadyForGameEvent.EnteredPeople(it))},
                    placehoder = "4명"
                )
            }
            item { Spacer(modifier = Modifier.height(14.dp)) }
            item {
                GameSettingSelect(
                    title = "목표 점수",
                    text = viewModel.score.value,
                    onValueChange = { viewModel.onEvent(ReadyForGameEvent.EnteredScore(it))},
                    placehoder = "100점"
                )
            }
        }
    }
}
@Composable
fun GameSettingSelect(
    title: String,
    text: String,
    onValueChange: (String) -> Unit,
    placehoder: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = typography.displayLarge,
            modifier = Modifier.padding(end = 20.dp)
        )
        SanjiniTextField(
            text = text,
            onValueChange = onValueChange,
            placeholder = placehoder
        )
    }
}

@Composable
fun GameSelect(
    options: List<SelectionOption> = emptyList(),
    onOptionClicked: (SelectionOption) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ){
        options.forEach { option ->
            GameElement(
                title = option.title,
                description = option.description,
                id = option.id,
                onClick = { onOptionClicked(option) },
                isChecked = option.selected,
            )
        }
    }
}

@Composable
fun GameElement(
    title: String,
    description: String,
    @DrawableRes id: Int,
    onClick: () -> Unit = {},
    isChecked: Boolean
){
    val img = if (isChecked) R.drawable.checked else R.drawable.unchecked
    val borderModifier = if (isChecked) Modifier.border(3.dp, main_yellow, MaterialTheme.shapes.large) else Modifier

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    ){
        Image(
            painter = painterResource(id),
            contentDescription = "game_image",
            modifier = borderModifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 56.dp)
                .align(Alignment.CenterStart)
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
        Image(
            painter = painterResource(img),
            contentDescription = "checked: $isChecked",
            modifier = Modifier
                .padding(end = 30.dp)
                .size(31.dp)
                .align(Alignment.CenterEnd)
        )
    }
}