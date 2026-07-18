package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.romisspa.app.domain.model.Cita

@Entity(tableName = "citas")
data class CitaEntity(
    @PrimaryKey val id: String,
    val cliente: String,
    val telefono: String,
    val servicio: String,
    val fecha: String,
    val hora: String,
    val empleadoId: String?,
    val estado: String,
    val colorArgb: Int
)

fun CitaEntity.toDomain() = Cita(
    id = id,
    cliente = cliente,
    telefono = telefono,
    servicio = servicio,
    fecha = fecha,
    hora = hora,
    empleadoId = empleadoId,
    estado = estado,
    colorEstado = Color(colorArgb)
)

fun Cita.toEntity() = CitaEntity(
    id = id,
    cliente = cliente,
    telefono = telefono,
    servicio = servicio,
    fecha = fecha,
    hora = hora,
    empleadoId = empleadoId,
    estado = estado,
    colorArgb = colorEstado.toArgb()
)
