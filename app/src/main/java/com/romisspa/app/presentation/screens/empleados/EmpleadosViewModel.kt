package com.romisspa.app.presentation.screens.empleados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.presentation.event.EventBus
import com.romisspa.app.presentation.event.UiEvent
import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmpleadosViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmpleadosUiState())
    val uiState: StateFlow<EmpleadosUiState> = _uiState.asStateFlow()

    init {
        loadEmpleados()
        loadVentas()
    }

    fun loadEmpleados() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val empleados = useCases.getEmpleados()
                _uiState.update { it.copy(empleados = empleados, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al cargar empleados") }
                EventBus.send(UiEvent.Error("Error al cargar: ${e.message}"))
            }
        }
    }

    private fun loadVentas() {
        viewModelScope.launch {
            try {
                val ventas = useCases.getVentas()
                _uiState.update { it.copy(ventas = ventas) }
            } catch (e: Exception) {
                // No bloquear la pantalla si fallan las ventas
            }
        }
    }

    fun addEmpleado(nombre: String, especialidad: String, telefono: String, comision: Double) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true) }
            try {
                val nuevoEmpleado = Empleado(
                    id = "", // Backend handles ID
                    nombre = nombre,
                    especialidad = especialidad,
                    telefono = telefono,
                    comision = comision
                )
                useCases.addEmpleado(nuevoEmpleado)
                EventBus.send(UiEvent.Success("Empleado agregado correctamente"))
                kotlinx.coroutines.delay(1000) // Esperar a que el backend se sincronice
                loadEmpleados()
            } catch (e: Exception) {
                _uiState.update { it.copy(isActionLoading = false, error = e.message ?: "Error al agregar empleado") }
                EventBus.send(UiEvent.Error("Error al agregar: ${e.message}"))
            }
        }
    }

    fun updateEmpleado(empleado: Empleado) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true) }
            try {
                useCases.updateEmpleado(empleado)
                kotlinx.coroutines.delay(500)
                loadEmpleados()
            } catch (e: Exception) {
                _uiState.update { it.copy(isActionLoading = false, error = e.message ?: "Error al actualizar empleado") }
            }
        }
    }

    fun deleteEmpleado(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isActionLoading = true) }
            try {
                useCases.deleteEmpleado(id)
                kotlinx.coroutines.delay(500)
                loadEmpleados()
            } catch (e: Exception) {
                _uiState.update { it.copy(isActionLoading = false, error = e.message ?: "Error al eliminar empleado") }
            }
        }
    }
}
