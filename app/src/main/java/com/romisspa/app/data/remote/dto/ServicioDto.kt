package com.romisspa.app.data.remote.dto

data class ServicioDto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int? = null
)
