package com.romisspa.app.di

import com.romisspa.app.domain.usecase.*

class UseCaseModule(repositoryModule: RepositoryModule) {
    val spaUseCases by lazy {
        SpaUseCases(
            getServicios = GetServiciosUseCase(repositoryModule.spaRepository),
            addServicio = AddServicioUseCase(repositoryModule.spaRepository),
            updateServicio = UpdateServicioUseCase(repositoryModule.spaRepository),
            deleteServicio = DeleteServicioUseCase(repositoryModule.spaRepository),
            getCitas = GetCitasUseCase(repositoryModule.spaRepository),
            addCita = AddCitaUseCase(repositoryModule.spaRepository),
            deleteCita = DeleteCitaUseCase(repositoryModule.spaRepository),
            getClientes = GetClientesUseCase(repositoryModule.spaRepository),
            addOrUpdateCliente = AddOrUpdateClienteUseCase(repositoryModule.spaRepository)
        )
    }
}
