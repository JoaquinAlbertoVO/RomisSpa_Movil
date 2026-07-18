package com.romisspa.app.data.mapper

import com.romisspa.app.data.remote.dto.VentaDto
import com.romisspa.app.domain.model.Venta

fun VentaDto.toDomain(): Venta {
    return Venta(
        id = id,
        cliente = cliente,
        servicio = servicio,
        monto = monto,
        empleadoId = empleadoId,
        metodoPago = metodoPago,
        fecha = fecha
    )
}

fun Venta.toDto(): VentaDto {
    return VentaDto(
        id = id,
        cliente = cliente,
        servicio = servicio,
        monto = monto,
        empleadoId = empleadoId,
        metodoPago = metodoPago,
        fecha = fecha
    )
}
