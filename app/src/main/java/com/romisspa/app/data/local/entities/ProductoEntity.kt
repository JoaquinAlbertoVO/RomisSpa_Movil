package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romisspa.app.domain.model.Producto

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Double,
    val categoria: String
)

fun ProductoEntity.toDomain() = Producto(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    stock = stock,
    categoria = categoria
)

fun Producto.toEntity() = ProductoEntity(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    stock = stock,
    categoria = categoria
)
