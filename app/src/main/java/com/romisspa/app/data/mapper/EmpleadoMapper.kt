package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.EmpleadoDto
import com.romisspa.app.domain.model.Empleado

fun EmpleadoDto.toDomain(): Empleado {
    return Empleado(
        id = id ?: "",
        nombre = nombre,
        especialidad = especialidad,
        telefono = telefono,
        comision = comision
    )
}

fun Empleado.toDto(): EmpleadoDto {
    return EmpleadoDto(
        id = id.ifEmpty { null },
        nombre = nombre,
        especialidad = especialidad,
        telefono = telefono,
        comision = comision
    )
}
