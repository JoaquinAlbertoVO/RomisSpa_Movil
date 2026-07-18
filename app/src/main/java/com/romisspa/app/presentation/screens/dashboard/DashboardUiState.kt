package com.romisspa.app.presentation.screens.dashboard

import com.romisspa.app.domain.model.Producto

data class DashboardUiState(
    val totalVentas: String = "S/ 0.00",
    val totalClientes: String = "0",
    val citasPendientes: String = "0",
    val stockAlerts: List<Producto> = emptyList(),
    val isLoading: Boolean = false
)
