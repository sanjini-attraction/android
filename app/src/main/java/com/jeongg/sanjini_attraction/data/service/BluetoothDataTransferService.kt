package com.jeongg.sanjini_attraction.data.service

import android.bluetooth.BluetoothSocket
import com.jeongg.sanjini_attraction.data.exception.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.ByteBuffer

class BluetoothDataTransferService(
    private val socket: BluetoothSocket
) {

    fun listenForIncomingMessages(): Flow<String> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw TransferFailedException()
                }
                emit( buffer[0].toInt().toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext false
            } catch (e: Exception){
                e.printStackTrace()
                return@withContext false
            }
            true
        }
    }
}