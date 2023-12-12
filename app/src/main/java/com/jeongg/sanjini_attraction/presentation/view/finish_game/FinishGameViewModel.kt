package com.jeongg.sanjini_attraction.presentation.view.finish_game

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongg.sanjini_attraction.domain.model.GameResult
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import com.jeongg.sanjini_attraction.presentation.util.SanjiniEvent
import com.jeongg.sanjini_attraction.presentation.view.game.FINISH_MESSAGE
import com.jeongg.sanjini_attraction.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class FinishGameViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
): ViewModel() {
    private val _eventFlow = MutableStateFlow<SanjiniEvent>(SanjiniEvent.LOADING)
    val eventFlow = _eventFlow

    private val _result = mutableStateOf<List<GameResult>>(emptyList())
    val result = _result

    private val _goal = mutableIntStateOf(0)
    val goal = _goal

    init {
        _goal.intValue = bluetoothRepository.score.value
        getResult()
    }

    private fun getResult() {
        viewModelScope.launch {
            try {
                var message = bluetoothRepository.messages.value.filter{isValid(it)}
                val peopleSize = bluetoothRepository.people.value
                if (message.size+1 != peopleSize){
                    message = message.subList(0, peopleSize)
                }
                val goal = bluetoothRepository.score.value

                _result.value = message.mapIndexed { index, s ->
                    val errorRate = abs(s.toInt() - goal)
                    GameResult(index+1, s.toInt(), errorRate, 0)
                }
                _result.value = _result.value.sortedBy { it.errorRate }
                var cnt = 0
                var prev = -1
                _result.value = _result.value.map{ s ->
                    if (prev != s.errorRate) {
                        prev = s.errorRate
                        cnt++
                    }
                    GameResult(s.people, s.score, s.errorRate, cnt)
                }
                eventFlow.emit(SanjiniEvent.FINISH("게임이 종료되었습니다."))
            } catch (e: Exception) {
                eventFlow.emit(SanjiniEvent.ERROR("결과를 불러오는데 문제가 발생했습니다."))
            }
        }
    }

    private fun isValid(message: String): Boolean{
        val regex = "^[0-9]\$|^[1-9][0-9]\$|^(100)\$".toRegex()
        return message != FINISH_MESSAGE && message.matches(regex)
    }

    fun disconnect(){
        try {
            bluetoothRepository.closeConnection()
        } catch (e: Exception){
            "연결 끊기 실패".log()
        }
    }

}