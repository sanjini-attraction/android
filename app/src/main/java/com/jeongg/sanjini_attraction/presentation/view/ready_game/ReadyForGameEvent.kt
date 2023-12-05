package com.jeongg.sanjini_attraction.presentation.view.ready_game

sealed class ReadyForGameEvent {
    data class EnteredPeople(val people: String): ReadyForGameEvent()
    data class EnteredScore(val score: String): ReadyForGameEvent()
    object StartGame: ReadyForGameEvent()
}
