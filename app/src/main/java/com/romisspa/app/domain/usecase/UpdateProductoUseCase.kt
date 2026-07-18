package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.repository.SpaRepository

class UpdateProductoUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(producto: Producto) {
        repository.updateProducto(producto)
    }
}
