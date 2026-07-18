package com.romisspa.app.presentation.screens.caja

data class CajaUiState(
    val montoApertura: Double = 0.0,
    val totalVentasDia: Double = 0.0,
    val ventasEfectivo: Double = 0.0,
    val ventasYape: Double = 0.0,
    val ventasPlin: Double = 0.0,
    val ventasTarjeta: Double = 0.0,
    val ventasTransferencia: Double = 0.0,
    val isLoading: Boolean = false,
    val isEditingApertura: Boolean = false,
    val error: String? = null
) {
    val totalEsperadoEfectivo: Double
        get() = montoApertura + ventasEfectivo
}
