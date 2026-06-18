package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository

class AddServicioUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(servicio: Servicio) {
        repository.addServicio(servicio)
    }
}
