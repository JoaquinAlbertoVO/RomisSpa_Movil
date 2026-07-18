package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.repository.SpaRepository

class GetProductosUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<Producto> {
        return repository.getProductos()
    }
}
