package com.jeongg.sanjini_attraction.domain.model

typealias BluetoothDeviceDomain = BluetoothDevice

data class BluetoothDevice(
    val name: String?,
    val address: String
)