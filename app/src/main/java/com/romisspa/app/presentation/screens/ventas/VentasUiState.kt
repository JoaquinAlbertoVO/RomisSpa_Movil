package com.romisspa.app.presentation.screens.ventas

import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Venta

data class VentasUiState(
    val ventas: List<Venta> = emptyList(),
    val filteredVentas: List<Venta> = emptyList(),
    val empleados: List<Empleado> = emptyList(),
    val isLoading: Boolean = false,
    val totalRecaudado: String = "S/ 0.00",
    val error: String? = null,
    
    // Filtros
    val selectedEmpleadoId: String? = null,
    val fechaInicio: String? = null,
    val fechaFin: String? = null
)
