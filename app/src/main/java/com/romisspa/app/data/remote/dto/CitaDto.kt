package com.romisspa.app.data.remote.dto

data class CitaDto(
    val id: String? = null,
    val cliente: String,
    val telefono: String? = null,
    val servicio: String,
    val fecha: String,
    val hora: String,
    val empleadoId: String? = null,
    val estado: String? = null
)
