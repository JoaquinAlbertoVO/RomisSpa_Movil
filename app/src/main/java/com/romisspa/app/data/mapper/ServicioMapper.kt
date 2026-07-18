package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.ServicioDto
import com.romisspa.app.domain.model.Insumo
import com.romisspa.app.domain.model.Servicio

fun ServicioDto.toDomain() = Servicio(
    id = id ?: "",
    nombre = nombre,
    descripcion = descripcion,
    precio = "S/ " + precio,
    duracion = "60 min",
    insumos = insumos.mapNotNull { map ->
        val productoId = map["productoId"] as? String ?: return@mapNotNull null
        val nombre = map["nombre"] as? String ?: return@mapNotNull null
        val cantidad = (map["cantidad"] as? Number)?.toDouble() ?: 1.0
        Insumo(productoId = productoId, nombre = nombre, cantidad = cantidad)
    }
)

fun Servicio.toDto() = ServicioDto(
    id = id.ifEmpty { null },
    nombre = nombre,
    descripcion = descripcion,
    precio = precio.replace("S/ ", "").toDoubleOrNull() ?: 0.0,
    imagenRes = null,
    insumos = insumos.map { insumo ->
        mapOf(
            "productoId" to insumo.productoId,
            "nombre" to insumo.nombre,
            "cantidad" to insumo.cantidad
        )
    }
)
