package com.romisspa.app.presentation.screens.servicios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.usecase.SpaUseCases
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiciosViewModel(

    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiciosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getServicios()
        loadProductos()
    }

    fun getServicios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val servicios = useCases.getServicios()
                _uiState.update { it.copy(servicios = servicios, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
                EventBus.send(UiEvent.Error("Error al cargar servicios: ${e.message}"))
            }
        }
    }

    fun loadProductos() {
        viewModelScope.launch {
            try {
                val productos = useCases.getProductos()
                _uiState.update { it.copy(productos = productos) }
            } catch (e: Exception) {
                // No crítico
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun onShowAddDialog(show: Boolean) {
        _uiState.update { it.copy(showAddDialog = show) }
    }

    fun addServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                useCases.addServicio(servicio) // Llama al caso de uso
                getServicios()                 // Recarga la lista en pantalla
                onShowAddDialog(false)         // Cierra el diálogo
                EventBus.send(UiEvent.Success("Servicio añadido correctamente"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al añadir servicio"))
            }
        }
    }

    fun deleteServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                useCases.deleteServicio(servicio)
                getServicios()
                EventBus.send(UiEvent.Success("Servicio eliminado"))
            } catch (e: Exception) {
                EventBus.send(UiEvent.Error("Error al eliminar servicio"))
            }
        }
    }
}
