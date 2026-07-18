package com.romisspa.app.domain.usecase

import com.romisspa.app.domain.model.RutinaDiaria
import com.romisspa.app.domain.model.TareaEspecial
import com.romisspa.app.domain.repository.SpaRepository

class GetRutinasDiariasUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<RutinaDiaria> = repository.getRutinasDiarias()
}

class AddOrUpdateRutinaDiariaUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(rutina: RutinaDiaria) = repository.addOrUpdateRutinaDiaria(rutina)
}

class DeleteRutinaDiariaUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(id: String) = repository.deleteRutinaDiaria(id)
}

class GetTareasEspecialesUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(): List<TareaEspecial> = repository.getTareasEspeciales()
}

class AddOrUpdateTareaEspecialUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(tarea: TareaEspecial) = repository.addOrUpdateTareaEspecial(tarea)
}

class DeleteTareaEspecialUseCase(private val repository: SpaRepository) {
    suspend operator fun invoke(id: String) = repository.deleteTareaEspecial(id)
}
