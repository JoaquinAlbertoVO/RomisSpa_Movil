package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Venta
import com.romisspa.app.domain.repository.SpaRepository

class GetVentasUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<Venta> = repository.getVentas()
}
