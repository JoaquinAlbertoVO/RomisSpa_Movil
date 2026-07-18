package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romisspa.app.domain.model.Empleado

@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val especialidad: String,
    val telefono: String,
    val comision: Double
)

fun EmpleadoEntity.toDomain() = Empleado(id, nombre, especialidad, telefono, comision)
fun Empleado.toEntity() = EmpleadoEntity(id, nombre, especialidad, telefono, comision)
