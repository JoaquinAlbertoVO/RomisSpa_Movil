package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.repository.SpaRepository

class AddOrUpdateClienteUseCase(private val repository: SpaRepository) {
    operator fun invoke(cliente: Cliente) {
        repository.addOrUpdateCliente(cliente)
    }
}
