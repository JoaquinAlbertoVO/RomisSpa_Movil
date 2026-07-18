package com.romisspa.app.presentation.event

sealed interface UiEvent {
    data class Success(val message: String) : UiEvent
    data class Error(val message: String) : UiEvent
    data class Warning(val message: String) : UiEvent
    data class OpenWhatsApp(val phone: String, val message: String) : UiEvent
}
