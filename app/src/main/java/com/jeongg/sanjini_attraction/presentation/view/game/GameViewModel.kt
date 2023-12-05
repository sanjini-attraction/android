package com.jeongg.sanjini_attraction.presentation.view.game

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeongg.sanjini_attraction.domain.model.ConnectionResult
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import com.jeongg.sanjini_attraction.presentation.state.BluetoothUiState
import com.jeongg.sanjini_attraction.util.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bluetoothRepository: BluetoothRepository,
): ViewModel() {

    private var index = mutableIntStateOf(0)

    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothRepository.messages,
        bluetoothRepository.pairedDevices,
        _state
    ) { messages, pairedDevices, state ->
        messages.toString().log()
        state.copy(
            pairedDevices = pairedDevices,
            messages = messages
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    private val _title = mutableStateOf("")
    val title = _title

    private val _description = mutableStateOf("")
    val description = _description

    private val options = listOf(
        Pair("퍽 굴리기", "주어진 목표지점으로부터 k만큼 떨어지도록 퍽이 위치하도록 굴려주세요"),
        Pair("미니 펀치 머신", "k만큼의 펀치력으로 미니 펀치 머신을 구부려주세요"),
        Pair("시간 맞히기", "k초가 됐다 싶으면 버튼을 눌러 종료해주세요."),
    )

    private var deviceConnectionJob: Job? = null

    init {
        bluetoothRepository.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothRepository.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = null
            ) }
        }.launchIn(viewModelScope)

        savedStateHandle.get<Int>("index")?.let {
            index.intValue = it
        }
        _title.value = options[index.intValue].first
        _description.value = options[index.intValue].second
        deviceConnectionJob = bluetoothRepository.getResult().listen()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                is ConnectionResult.TransferSucceeded -> {
                    "result in viewModel ${result.message}".log()
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                    "result in viewModel2 ${state.value.messages.toString()}".log()
                }
                is ConnectionResult.Error -> {
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = result.message
                    ) }
                }
                else -> {}
            }
        }
            .catch { throwable ->
                bluetoothRepository.closeConnection()
                _state.update { it.copy(
                    isConnected = false,
                    isConnecting = false,
                ) }
            }
            .launchIn(viewModelScope)
    }

}