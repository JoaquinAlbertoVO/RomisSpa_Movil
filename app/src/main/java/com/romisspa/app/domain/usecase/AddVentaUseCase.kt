package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Venta
import com.romisspa.app.domain.repository.SpaRepository

class AddVentaUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(venta: Venta) = repository.addVenta(venta)
}
