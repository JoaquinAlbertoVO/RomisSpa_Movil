package com.romisspa.app.domain.model

data class Empleado(
    val id: String = "",
    val nombre: String,
    val especialidad: String,
    val telefono: String,
    val comision: Double // Porcentaje de comisión
)
