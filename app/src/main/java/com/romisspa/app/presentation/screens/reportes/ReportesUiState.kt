package com.romisspa.app.presentation.screens.reportes

import com.romisspa.app.domain.model.Venta

data class ReportesUiState(
    val isLoading: Boolean = false,
    val totalVentasMes: Double = 0.0,
    val totalVentasSemana: Double = 0.0,
    val comisionesSemana: Map<String, Double> = emptyMap(),
    val ventasPorMetodoPago: Map<String, Double> = emptyMap(),
    val comisionesPorEmpleado: Map<String, Double> = emptyMap(),
    val detallesComisionesSemana: Map<String, List<Venta>> = emptyMap(),
    val gananciaAdministracionMes: Double = 0.0,
    val gananciaAdministracionSemana: Double = 0.0,
    val serviciosMasSolicitados: List<Pair<String, Int>> = emptyList(),
    val error: String? = null
)
