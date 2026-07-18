package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Empleado
import com.romisspa.app.domain.repository.SpaRepository

class AddEmpleadoUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(empleado: Empleado) {
        repository.addEmpleado(empleado)
    }
}
