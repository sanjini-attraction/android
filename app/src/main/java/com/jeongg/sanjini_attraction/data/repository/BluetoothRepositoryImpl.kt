package com.jeongg.sanjini_attraction.data.repository

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.jeongg.sanjini_attraction.data.service.BluetoothDataTransferService
import com.jeongg.sanjini_attraction.data.service.BluetoothStateReceiverService
import com.jeongg.sanjini_attraction.domain.mapper.toBluetoothDeviceDomain
import com.jeongg.sanjini_attraction.domain.model.BluetoothDeviceDomain
import com.jeongg.sanjini_attraction.domain.model.ConnectionResult
import com.jeongg.sanjini_attraction.domain.repository.BluetoothRepository
import com.jeongg.sanjini_attraction.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothRepositoryImpl(
    private val context: Context
): BluetoothRepository {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var dataTransferService: BluetoothDataTransferService? = null

    private val _people = MutableStateFlow(0)
    override val people: StateFlow<Int>
        get() = _people.asStateFlow()

    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    override val messages: StateFlow<List<String>>
        get() = _messages.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val _errors = MutableSharedFlow<String>()
    override val errors: SharedFlow<String>
        get() = _errors.asSharedFlow()

    private val bluetoothStateReceiverService = BluetoothStateReceiverService { isConnected, bluetoothDevice ->
        if(bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _errors.emit("Can't connect to a non-paired device.")
            }
        }
    }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        updatePairedDevices()
        context.registerReceiver(
            bluetoothStateReceiverService,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }

    override fun startDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun setPeople(people: Int){
        _people.update { people }
    }

    override fun setScore(score: Int) {
        _score.update { score }
    }

    override fun removeMessages() {
        _messages.update { emptyList() }
    }

    override fun connectToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        return flow {
            if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No BLUETOOTH_CONNECT permission")
            }

            val myDevice = bluetoothAdapter?.getRemoteDevice(device.address)
            val myUUID: UUID? = myDevice?.uuids?.get(0)?.uuid
            currentClientSocket = myDevice
                ?.createInsecureRfcommSocketToServiceRecord(
                    myUUID
                )
            stopDiscovery()
            currentClientSocket?.let { socket ->
                socket.connect()
                "연결 성공".log()
                emit(ConnectionResult.ConnectionEstablished)
            }
            getMessage()
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun getResult(): Flow<ConnectionResult>{
        return flow {
            getMessage()
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<ConnectionResult>.getMessage() {
        currentClientSocket?.let { socket ->
            try {
                BluetoothDataTransferService(socket).also {
                    dataTransferService = it
                    emitAll(
                        it.listenForIncomingMessages()
                            .map { message ->
                                _messages.update { m -> m.plus(message) }
                                ConnectionResult.TransferSucceeded(message)
                            }
                    )
                }
            } catch (e: IOException) {
                socket.close()
                currentClientSocket = null
                emit(ConnectionResult.Error("해당 Device와의 연결에 실패했습니다."))
            }
        }
    }

    override suspend fun trySendMessage(message: String): String? {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return null
        }

        if(dataTransferService == null) {
            return null
        }
        dataTransferService?.sendMessage(message.toByteArray())
        return message
    }

    override fun closeConnection() {
        _messages.value = emptyList()
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentClientSocket = null
        currentServerSocket = null
    }

    override fun release() {
        context.unregisterReceiver(bluetoothStateReceiverService)
        closeConnection()
    }

    private fun updatePairedDevices() {
        if(!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices ->
                _pairedDevices.update { devices }
            }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

}