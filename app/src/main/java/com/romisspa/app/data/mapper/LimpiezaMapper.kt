package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.RutinaDiariaDto
import com.romisspa.app.data.remote.dto.TareaEspecialDto
import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial

fun RutinaDiariaDto.toDomain(): RutinaDiaria {
    return RutinaDiaria(
        id = id,
        tarea = tarea,
        empleadoId = empleadoId,
        empleadoNombre = empleadoNombre,
        fechaActualizacion = fechaActualizacion,
        estadoActual = estadoActual
    )
}

fun RutinaDiaria.toDto(): RutinaDiariaDto {
    return RutinaDiariaDto(
        id = id,
        tarea = tarea,
        empleadoId = empleadoId,
        empleadoNombre = empleadoNombre,
        fechaActualizacion = fechaActualizacion,
        estadoActual = estadoActual
    )
}

fun TareaEspecialDto.toDomain(): TareaEspecial {
    return TareaEspecial(
        id = id,
        tarea = tarea,
        empleadoId = empleadoId,
        empleadoNombre = empleadoNombre,
        fechaAsignada = fechaAsignada,
        estado = estado
    )
}

fun TareaEspecial.toDto(): TareaEspecialDto {
    return TareaEspecialDto(
        id = id,
        tarea = tarea,
        empleadoId = empleadoId,
        empleadoNombre = empleadoNombre,
        fechaAsignada = fechaAsignada,
        estado = estado
    )
}
