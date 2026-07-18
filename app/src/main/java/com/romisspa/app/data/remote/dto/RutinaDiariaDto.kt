package com.romisspa.app.data.remote.dto

data class RutinaDiariaDto(
    val id: String = "",
    val tarea: String = "",
    val empleadoId: String = "",
    val empleadoNombre: String = "",
    val fechaActualizacion: String = "",
    val estadoActual: String = "PENDIENTE"
)
