package com.romisspa.app.presentation.screens.reserva

data class ReservaUiState(
    val clienteNombre: String = "",
    val clienteTelefono: String = "",
    val servicioSelected: String = "",
    val empleadaSelected: String = "",
    val fechaSelected: String = "",
    val horaSelected: String = "",
    val notasExtra: String = "",
    val isNuevoCliente: Boolean = true,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
