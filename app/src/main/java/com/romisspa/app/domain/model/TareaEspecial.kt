package com.romisspa.app.domain.model

data class TareaEspecial(
    val id: String = "",
    val tarea: String = "",
    val empleadoId: String = "",
    val empleadoNombre: String = "",
    val fechaAsignada: String = "", // "yyyy-MM-dd"
    val estado: String = "PENDIENTE" // PENDIENTE, POR_SUPERVISAR, COMPLETADA
)
