package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Producto
import com.romisspa.app.domain.repository.SpaRepository

class AddProductoUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(producto: Producto) {
        repository.addProducto(producto)
    }
}
