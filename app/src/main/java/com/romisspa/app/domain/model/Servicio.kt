package com.romisspa.app.domain.model

data class Servicio(
    val id: String = "",
    val nombre: String,
    val descripcion: String,
    val precio: String,
    val duracion: String,
    val insumos: List<Insumo> = emptyList()
)
