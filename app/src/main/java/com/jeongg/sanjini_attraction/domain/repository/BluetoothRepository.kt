package com.jeongg.sanjini_attraction.domain.repository

import com.jeongg.sanjini_attraction.domain.model.BluetoothDevice
import com.jeongg.sanjini_attraction.domain.model.ConnectionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothRepository {
    val isConnected: StateFlow<Boolean>
    val messages: StateFlow<List<String>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>
    val errors: SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()

    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>
    suspend fun trySendMessage(message: String): String?

    fun closeConnection()
    fun release()
    fun getResult(): Flow<ConnectionResult>
}