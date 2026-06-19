package com.romisspa.app.data.remote.dto

data class CreateServicioRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int? = null
)
