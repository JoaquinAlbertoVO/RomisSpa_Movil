package com.romisspa.app.data.remote.dto

data class CreateVentaRequest(
    val cliente: String,
    val servicio: String,
    val monto: Double,
    val empleadoId: String?,
    val metodoPago: String
)
