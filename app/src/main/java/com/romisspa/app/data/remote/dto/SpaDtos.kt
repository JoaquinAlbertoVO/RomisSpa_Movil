package com.romisspa.app.data.remote.dto

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio

// DTOs (Coinciden con el JSON de la API)
data class ServicioDto(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenRes: Int? = null
)

data class ClienteDto(
    val nombre: String,
    val telefono: String
)

data class CitaDto(
    val cliente: String,
    val servicio: String,
    val fecha: String,
    val hora: String
)

// Mappers (Conversión a Dominio)
fun ServicioDto.toDomain() = Servicio(nombre, descripcion, precio, imagenRes)
fun Servicio.toDto() = ServicioDto(nombre, descripcion, precio, imagenRes)

fun ClienteDto.toDomain() = Cliente(nombre, telefono)
fun Cliente.toDto() = ClienteDto(nombre, telefono)

fun CitaDto.toDomain() = Cita(cliente, servicio, fecha, hora)
fun Cita.toDto() = CitaDto(cliente, servicio, fecha, hora)
