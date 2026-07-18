
package com.romisspa.app.data.mapper

import androidx.compose.ui.graphics.Color
import com.romisspa.app.data.remote.dto.CitaDto
import com.romisspa.app.domain.model.Cita

fun CitaDto.toDomain() = Cita(
    id = id ?: "",
    cliente = cliente,
    telefono = telefono ?: "",
    servicio = servicio,
    fecha = fecha,
    hora = hora,
    empleadoId = empleadoId,
    estado = estado ?: "Pendiente",
    colorEstado = when (estado ?: "Pendiente") {
        "Pendiente" -> Color(0xFFD4AF37) // Dorado suave (WarmGold)
        "Confirmada" -> Color(0xFF4CAF50) // Verde
        "Atendida" -> Color(0xFF9E9E9E) // Gris
        else -> Color.Gray
    }
)

fun Cita.toDto() = CitaDto(
    id = id,
    cliente = cliente,
    telefono = telefono,
    servicio = servicio,
    fecha = fecha,
    hora = hora,
    empleadoId = empleadoId,
    estado = estado
)
