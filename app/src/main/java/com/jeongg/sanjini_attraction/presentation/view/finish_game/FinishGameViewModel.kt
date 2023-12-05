package com.jeongg.sanjini_attraction.presentation.view.finish_game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import com.jeongg.sanjini_attraction.presentation.util.SanjiniEvent
import com.jeongg.sanjini_attraction.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FinishGameViewModel @Inject constructor(
    private val bluetoothRepository: BluetoothRepository
): ViewModel() {
    private val _eventFlow = MutableStateFlow<SanjiniEvent>(SanjiniEvent.LOADING)
    val eventFlow = _eventFlow

    private val _result = mutableStateOf<List<String>>(emptyList())
    val result = _result

    init {
        _result.value = bluetoothRepository.messages.value
    }

    fun disconnect(){
        try {
            bluetoothRepository.closeConnection()
        } catch (e: Exception){
            "연결 끊기 실패".log()
        }
    }

}