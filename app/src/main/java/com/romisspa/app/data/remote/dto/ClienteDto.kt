package com.romisspa.app.data.remote.dto

data class ClienteDto(
    val nombre: String,
    val telefono: String,
    val ultimaVisita: String? = null,
    val totalVisitas: Int? = null
)
