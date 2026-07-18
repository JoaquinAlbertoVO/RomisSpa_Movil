package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.repository.SpaRepository

class UpdateCitaStatusUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(citaId: String, nuevoEstado: String) {
        repository.updateCitaStatus(citaId, nuevoEstado)
    }
}
