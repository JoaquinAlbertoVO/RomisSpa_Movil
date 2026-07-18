package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.ProductoDto
import com.romisspa.app.domain.model.Producto

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = id ?: "",
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        categoria = categoria
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = id.ifEmpty { null },
        nombre = nombre,
        descripcion = descripcion,
        precio = precio,
        stock = stock,
        categoria = categoria
    )
}
