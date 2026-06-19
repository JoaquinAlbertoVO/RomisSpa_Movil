package com.romisspa.app.domain.model

import androidx.compose.ui.graphics.Color

data class Cita(
    val id: String = "",
    val cliente: String,
    val servicio: String,
    val fecha: String,
    val hora: String,
    val estado: String = "Pendiente",
    val colorEstado: Color = Color.Gray
)
