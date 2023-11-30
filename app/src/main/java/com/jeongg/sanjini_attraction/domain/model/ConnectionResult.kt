package com.jeongg.sanjini_attraction.domain.model

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class TransferSucceeded(val message: String): ConnectionResult
    data class Error(val message: String): ConnectionResult
}