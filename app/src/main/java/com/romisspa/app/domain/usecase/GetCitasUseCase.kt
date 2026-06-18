package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.repository.SpaRepository

class GetCitasUseCase(private val repository: SpaRepository) {
    operator fun invoke(): List<Cita> = repository.getCitas()
}
