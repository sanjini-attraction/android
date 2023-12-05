package com.jeongg.sanjini_attraction.presentation.view.bluetooth

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.jeongg.sanjini_attraction.presentation.component.ProgressIndicator
import com.jeongg.sanjini_attraction.presentation.component.SanjiniDivider
import com.jeongg.sanjini_attraction.presentation.component.SanjiniTitle
import com.jeongg.sanjini_attraction.presentation.navigation.Screen
import com.jeongg.sanjini_attraction.ui.theme.typography

@SuppressLint("MissingPermission")
@Composable
fun BluetoothScreen(
    navController: NavController,
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = state.errorMessage) {
        state.errorMessage?.let { message ->
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }
    }
    when{
        state.isConnecting -> ProgressIndicator()
        state.isConnected -> { navController.navigate(Screen.ReadyForGameScreen.route) }
    }
    SanjiniTitle(
        modifier = Modifier.fillMaxSize(),
        title = "블루투스 연결하기",
        description = "블루투스 권한 허용하기 → THU_08과 페어링 완료하기 → 페어링된 디바이스 클릭하기"
    ){
        LazyColumn {
            item {
                Text(
                    text = "페어링 완료된 기기",
                    style = typography.titleMedium,
                    modifier = Modifier.padding(top = 25.dp)
                )
            }
            item{ SanjiniDivider(modifier = Modifier.padding(top = 7.dp, bottom = 15.dp)) }
            items(state.pairedDevices) { device ->
                Text(
                    text = device.name ?: "이름 없음",
                    style = typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                        .clickable {
                            viewModel.connectToDevice(device)
                        }
                )
            }
        }
    }
}