package com.jeongg.sanjini_attraction.presentation.view.bluetooth

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeongg.sanjini_attraction.presentation.state.BluetoothUiState

@SuppressLint("MissingPermission")
@Composable
fun BluetoothConnect(
    state: BluetoothUiState,
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
        items(state.pairedDevices) { device ->
            Text(
                text = device.name ?: "(No name)",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{
                        viewModel.connectToDevice(device)
                    }
                    .padding(16.dp)
            )
        }

    }

}