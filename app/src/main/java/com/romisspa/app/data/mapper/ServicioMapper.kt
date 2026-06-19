package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.ServicioDto
import com.romisspa.app.domain.model.Servicio

fun ServicioDto.toDomain() = Servicio(
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenRes = imagenRes
)

fun Servicio.toDto() = ServicioDto(
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagenRes = imagenRes
)
