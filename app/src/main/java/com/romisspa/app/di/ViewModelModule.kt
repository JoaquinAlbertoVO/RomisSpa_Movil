package com.romisspa.app.di

import com.romisspa.app.presentation.screens.reserva.ReservaViewModel
import com.romisspa.app.presentation.screens.servicios.ServiciosViewModel
import com.romisspa.app.presentation.screens.citas.CitasViewModel
import com.romisspa.app.presentation.screens.clientes.ClientesViewModel

class ViewModelModule(private val useCaseModule: UseCaseModule) {

    fun provideReservaViewModel(): ReservaViewModel {
        return ReservaViewModel(useCaseModule.spaUseCases)
    }

    fun provideServiciosViewModel(): ServiciosViewModel {
        return ServiciosViewModel(useCaseModule.spaUseCases)
    }

    fun provideCitasViewModel(): CitasViewModel {
        return CitasViewModel(useCaseModule.spaUseCases)
    }

    fun provideClientesViewModel(): ClientesViewModel {
        return ClientesViewModel(useCaseModule.spaUseCases)
    }
}
