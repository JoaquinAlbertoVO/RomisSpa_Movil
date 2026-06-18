package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository

class DeleteServicioUseCase(private val repository: SpaRepository) {
    operator fun invoke(servicio: Servicio) {
        repository.deleteServicio(servicio)
    }
}
