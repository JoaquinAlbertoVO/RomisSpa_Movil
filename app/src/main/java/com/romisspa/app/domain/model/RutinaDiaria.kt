package com.romisspa.app.domain.model

data class RutinaDiaria(
    val id: String = "",
    val tarea: String = "",
    val empleadoId: String = "", // ID of the employee assigned
    val empleadoNombre: String = "", // Name of employee for easy display
    val fechaActualizacion: String = "", // Format: "yyyy-MM-dd"
    val estadoActual: String = "PENDIENTE" // PENDIENTE, POR_SUPERVISAR, COMPLETADA
)
