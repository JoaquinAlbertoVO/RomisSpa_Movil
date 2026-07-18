package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.Insumo
import com.romisspa.app.domain.repository.SpaRepository

class DescontarInsumosUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(insumosUsados: List<Insumo>) =
        repository.descontarInsumos(insumosUsados)
}
