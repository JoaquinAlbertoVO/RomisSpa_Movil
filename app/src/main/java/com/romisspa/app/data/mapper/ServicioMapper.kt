package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.ServicioDto
import com.romisspa.app.domain.model.Servicio

fun ServicioDto.toDomain() = Servicio(
    nombre = nombre,
    descripcion = descripcion,
    // Convertimos el número a texto y le agregamos "S/ " adelante
    precio = "S/ " + precio,
    // Como la API no nos da duración todavía, le ponemos uno por defecto
    duracion = "60 min"
)

fun Servicio.toDto() = ServicioDto(
    nombre = nombre,
    descripcion = descripcion,
    // Quitamos el "S/ " del texto y lo convertimos a número decimal (Double)
    // Si el texto no es un número, usamos 0.0 para que no falle
    precio = precio.replace("S/ ", "").toDoubleOrNull() ?: 0.0,
    // Por ahora no enviamos imagen, así que mandamos null
    imagenRes = null
)
