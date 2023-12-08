package com.jeongg.sanjini_attraction.presentation.util

sealed class SanjiniEvent {
    object SUCCESS: SanjiniEvent()
    object LOADING: SanjiniEvent()
    data class FINISH(val message: String): SanjiniEvent()
    data class ERROR(val message: String): SanjiniEvent()
}