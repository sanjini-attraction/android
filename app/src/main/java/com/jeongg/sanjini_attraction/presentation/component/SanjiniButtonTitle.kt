package com.jeongg.sanjini_attraction.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeongg.sanjini_attraction.ui.theme.Dimens

@Composable
fun SanjiniButtonTitle(
    modifier: Modifier = Modifier,
    buttonText: String,
    onButtonClick: () -> Unit = {},
    title: String,
    description: String,
    content: @Composable (ColumnScope.() -> Unit),
){
    Box(
        modifier = modifier.padding(Dimens.ButtonScreenPadding)
    ){
        Column(
            modifier = Modifier.padding(bottom = 50.dp)
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            SanjiniDivider(modifier = Modifier.padding(top = 7.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 23.dp)
            )
            Column(content=content)
        }
        SanjiniButton(
            text = buttonText,
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = onButtonClick
        )
    }

}