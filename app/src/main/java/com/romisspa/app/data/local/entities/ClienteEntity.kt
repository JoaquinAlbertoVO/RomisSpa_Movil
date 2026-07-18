package com.romisspa.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.romisspa.app.domain.model.Cliente

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey val nombre: String,
    val telefono: String,
    val ultimaVisita: String,
    val totalVisitas: Int
)

fun ClienteEntity.toDomain() = Cliente(nombre, telefono, ultimaVisita, totalVisitas)
fun Cliente.toEntity() = ClienteEntity(nombre, telefono, ultimaVisita, totalVisitas)
