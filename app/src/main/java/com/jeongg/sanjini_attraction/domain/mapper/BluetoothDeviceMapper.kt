package com.jeongg.sanjini_attraction.domain.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.jeongg.sanjini_attraction.domain.model.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain {
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )
}