package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.repository.SpaRepository

class GetClientesUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<Cliente> = repository.getClientes()
}
