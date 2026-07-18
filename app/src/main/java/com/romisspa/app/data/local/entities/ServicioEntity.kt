package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romisspa.app.domain.model.Servicio

@Entity(tableName = "servicios")
data class ServicioEntity(
    @PrimaryKey val nombre: String,
    val descripcion: String,
    val precio: String,
    val duracion: String = "60 min"
)

fun ServicioEntity.toDomain() = Servicio(
    id = "",
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    duracion = duracion
)

fun Servicio.toEntity() = ServicioEntity(
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    duracion = duracion
)
