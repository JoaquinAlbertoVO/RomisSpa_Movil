package com.romisspa.app.presentation.screens.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitasViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getCitas()
    }

    fun getCitas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val citas = useCases.getCitas()
                _uiState.update { it.copy(citas = citas, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar citas: ${e.message}"))
            }
        }
    }
}
