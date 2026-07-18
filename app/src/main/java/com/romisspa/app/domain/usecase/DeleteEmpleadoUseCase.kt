package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.repository.SpaRepository

class DeleteEmpleadoUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(empleadoId: String) {
        repository.deleteEmpleado(empleadoId)
    }
}
