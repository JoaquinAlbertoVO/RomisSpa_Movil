package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.repository.SpaRepository

class DeleteCitaUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(cita: Cita) {
        repository.deleteCita(cita)
    }
}
