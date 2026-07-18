package com.romisspa.app.presentation.screens.empleados

import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.model.Venta

data class EmpleadosUiState(
    val empleados: List<Empleado> = emptyList(),
    val ventas: List<Venta> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isActionLoading: Boolean = false
)
