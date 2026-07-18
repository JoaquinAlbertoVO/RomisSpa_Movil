package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romisspa.app.domain.model.Venta

@Entity(tableName = "ventas")
data class VentaEntity(
    @PrimaryKey val id: String,
    val cliente: String,
    val servicio: String,
    val monto: Double,
    val empleadoId: String?,
    val metodoPago: String,
    val fecha: String
)

fun VentaEntity.toDomain() = Venta(id, cliente, servicio, monto, empleadoId, metodoPago, fecha)
fun Venta.toEntity() = VentaEntity(id, cliente, servicio, monto, empleadoId, metodoPago, fecha)
