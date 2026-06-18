package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository

class GetServiciosUseCase(private val repository: SpaRepository) {
    operator fun invoke(): List<Servicio> = repository.getServicios()
}
