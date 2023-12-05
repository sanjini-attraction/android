package com.jeongg.sanjini_attraction.domain.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SelectionOption(
    val index: Int,
    @DrawableRes val id: Int,
    val title: String,
    val description: String,
    val initialSelectedValue: Boolean
) {
    var selected by mutableStateOf(initialSelectedValue)
}