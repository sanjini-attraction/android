package com.jeongg.sanjini_attraction.presentation.view.ready_game

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongg.sanjini_attraction.R
import com.jeongg.sanjini_attraction.domain.model.SelectionOption
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import com.jeongg.sanjini_attraction.presentation.util.SanjiniEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadyForGameViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
): ViewModel() {

    private val _eventFlow = MutableStateFlow<SanjiniEvent>(SanjiniEvent.LOADING)
    val eventFlow = _eventFlow

    private val _people = mutableStateOf("")
    val people = _people

    private val _score = mutableStateOf("")
    val score = _score

    private val _options = listOf(
        SelectionOption(0, R.drawable.puck, "퍽 굴리기", "정확하게 퍽을 굴려보세요.\n목표 점수는 해당 퍽에서 떨어진 거리예요.", true),
        SelectionOption(1, R.drawable.punch, "미니 펀치 머신", "목표 점수에 해당하는 만큼의 펀치력을 보여주세요.", false),
        SelectionOption(2, R.drawable.time, "시간 맞히기", "내가 생각하기에 목표 시간이 되면 버튼을 클릭해주세요.", false),
    ).toMutableStateList()

    val options: List<SelectionOption> get() = _options

    fun selectionOptionSelected(selectedOption: SelectionOption) {
        _options.forEach { it.selected = false }
        _options.find { it.title == selectedOption.title }?.selected = true
    }

    fun onEvent(event: ReadyForGameEvent){
        when (event){
            is ReadyForGameEvent.EnteredPeople -> {
                _people.value = event.people
            }
            is ReadyForGameEvent.EnteredScore -> {
                _score.value = event.score
            }
            is ReadyForGameEvent.StartGame -> {
                sendMessage()
            }
        }
    }
    fun getSelectedIndex(): Int{
        return _options.find { it.selected }?.index ?: 0
    }
    private fun sendMessage() {
        val index = getSelectedIndex()
        val message = "$index ${people.value} ${score.value}"

        viewModelScope.launch {
            if (!isValid(people.value) || !isValid(score.value)) {
                _eventFlow.emit(SanjiniEvent.ERROR("값은 1~100 사이의 숫자만 입력 가능합니다."))
                return@launch
            }
            val bluetoothMessage = bluetoothRepository.trySendMessage(message)
            if(bluetoothMessage != null) {
                _eventFlow.emit(SanjiniEvent.SUCCESS)
            } else {
                _eventFlow.emit(SanjiniEvent.ERROR("메시지 전송에 실패했습니다."))
            }
        }
    }

    private fun isValid(message: String): Boolean{
        val regex = "^[1-9]\$|^[1-9][0-9]\$|^(100)\$".toRegex()
        return message.matches(regex)
    }
}