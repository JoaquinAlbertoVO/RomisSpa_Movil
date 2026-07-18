package com.romisspa.app.presentation.screens.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.romisspa.app.domain.usecase.SpaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VentasViewModel(
    private val useCases: SpaUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(VentasUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val ventas = useCases.getVentas()
                val empleados = useCases.getEmpleados()
                _uiState.update { 
                    it.copy(
                        ventas = ventas.reversed(),
                        empleados = empleados,
                        isLoading = false 
                    ) 
                }
                applyFilters()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun onEmpleadoFilterChange(empleadoId: String?) {
        _uiState.update { it.copy(selectedEmpleadoId = empleadoId) }
        applyFilters()
    }

    fun onDateRangeChange(inicio: String?, fin: String?) {
        _uiState.update { it.copy(fechaInicio = inicio, fechaFin = fin) }
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.ventas

        // Filtrar por empleado
        if (currentState.selectedEmpleadoId != null) {
            filtered = filtered.filter { it.empleadoId == currentState.selectedEmpleadoId }
        }

        // Filtrar por rango de fechas (asumiendo formato YYYY-MM-DD)
        if (currentState.fechaInicio != null) {
            filtered = filtered.filter { it.fecha >= currentState.fechaInicio }
        }
        if (currentState.fechaFin != null) {
            filtered = filtered.filter { it.fecha <= currentState.fechaFin }
        }

        val total = filtered.sumOf { it.monto }
        _uiState.update { 
            it.copy(
                filteredVentas = filtered,
                totalRecaudado = "S/ ${"%.2f".format(total)}"
            )
        }
    }
}
