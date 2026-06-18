package com.romisspa.app.presentation.screens.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClientesViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getClientes()
    }

    fun getClientes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val clientes = useCases.getClientes()
                _uiState.update { it.copy(clientes = clientes, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar clientes: ${e.message}"))
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }
}
