package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.ClienteDto
import com.romisspa.app.domain.model.Cliente

fun ClienteDto.toDomain() = Cliente(
    nombre = nombre,
    telefono = telefono
)

fun Cliente.toDto() = ClienteDto(
    nombre = nombre,
    telefono = telefono
)
