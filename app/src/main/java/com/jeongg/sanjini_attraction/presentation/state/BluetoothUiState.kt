package com.jeongg.sanjini_attraction.presentation.state

import com.jeongg.sanjini_attraction.domain.model.BluetoothDevice

data class BluetoothUiState(
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<String> = emptyList()
)