package com.romisspa.app.data.remote.dto

data class TareaEspecialDto(
    val id: String = "",
    val tarea: String = "",
    val empleadoId: String = "",
    val empleadoNombre: String = "",
    val fechaAsignada: String = "",
    val estado: String = "PENDIENTE"
)
