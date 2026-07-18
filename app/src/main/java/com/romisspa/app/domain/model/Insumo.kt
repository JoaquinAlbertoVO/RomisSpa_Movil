package com.romisspa.app.domain.model

/**
 * Representa un insumo (producto) consumido en un servicio.
 * La cantidad usa Double para permitir fracciones: 0.5 (medio sachet), 1.5 (botella y media), etc.
 */
data class Insumo(
    val productoId: String,
    val nombre: String,
    val cantidad: Double = 1.0
)
