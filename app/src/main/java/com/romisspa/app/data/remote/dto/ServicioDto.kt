package com.romisspa.app.data.remote.dto

data class ServicioDto(
    val id: String? = null,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: String? = null,
    // Lista de insumos: cada Map tiene "productoId", "nombre", "cantidad"
    val insumos: List<Map<String, Any>> = emptyList()
)
