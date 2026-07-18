package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.repository.SpaRepository

class DeleteProductoUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(productoId: String) {
        repository.deleteProducto(productoId)
    }
}
