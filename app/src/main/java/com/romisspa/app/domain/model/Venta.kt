package com.romisspa.app.domain.model

data class Venta(
    val id: String = "",
    val cliente: String,
    val servicio: String,
    val monto: Double,
    val empleadoId: String? = null,
    val metodoPago: String = "Efectivo",
    val fecha: String = ""
)
