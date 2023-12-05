package com.jeongg.sanjini_attraction.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jeongg.sanjini_attraction.ui.theme.main_yellow

@Composable
fun SanjiniButton(
    modifier: Modifier = Modifier,
    text: String = "버튼",
    onClick: () -> Unit = {},
){
    Button(
        modifier = modifier.fillMaxWidth().height(44.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = main_yellow,
            contentColor = Color.Black,
        ),
        contentPadding = PaddingValues(0.dp)
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

