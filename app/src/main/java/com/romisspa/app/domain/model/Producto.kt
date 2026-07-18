package com.romisspa.app.domain.model

data class Producto(
    val id: String = "",
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Double,
    val categoria: String
)
