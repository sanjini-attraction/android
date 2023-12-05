package com.jeongg.sanjini_attraction.presentation.util

sealed class SanjiniEvent {
    object SUCCESS: SanjiniEvent()
    data class ERROR(val message: String): SanjiniEvent()
}