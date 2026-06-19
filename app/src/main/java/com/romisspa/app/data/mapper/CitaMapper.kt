package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.CitaDto
import com.romisspa.app.domain.model.Cita

fun CitaDto.toDomain() = Cita(
    id = id ?: "",
    cliente = cliente,
    servicio = servicio,
    fecha = fecha,
    hora = hora
)

fun Cita.toDto() = CitaDto(
    id = id,
    cliente = cliente,
    servicio = servicio,
    fecha = fecha,
    hora = hora
)
