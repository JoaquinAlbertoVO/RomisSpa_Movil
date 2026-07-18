package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.repository.SpaRepository

class GetEmpleadosUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<Empleado> {
        return repository.getEmpleados()
    }
}
