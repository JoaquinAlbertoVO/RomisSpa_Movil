package com.romisspa.app.di

import com.romisspa.app.presentation.screens.reserva.ReservaViewModel
import com.romisspa.app.presentation.screens.servicios.ServiciosViewModel
import com.romisspa.app.presentation.screens.citas.CitasViewModel
import com.romisspa.app.presentation.screens.clientes.ClientesViewModel

class AppContainer {
    // Necesitamos el modulo de red para el repositorio de producción
    private val networkModule = NetworkModule()
    private val repositoryModule = RepositoryModule(networkModule)
    private val useCaseModule = UseCaseModule(repositoryModule)

    val reservaViewModel by lazy {
        ReservaViewModel(useCaseModule.spaUseCases)
    }

    val serviciosViewModel by lazy {
        ServiciosViewModel(useCaseModule.spaUseCases)
    }

    val citasViewModel by lazy {
        CitasViewModel(useCaseModule.spaUseCases)
    }

    val clientesViewModel by lazy {
        ClientesViewModel(useCaseModule.spaUseCases)
    }
}
