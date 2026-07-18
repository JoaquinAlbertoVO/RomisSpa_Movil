package com.romisspa.app.domain.model

import androidx.compose.ui.graphics.Color

data class Cita(
    val id: String = "",
    val cliente: String,
    val telefono: String = "",
    val servicio: String,
    val fecha: String,
    val hora: String,
    val empleadoId: String? = null,
    val estado: String = "Pendiente",
    val colorEstado: Color = Color.Gray
)
