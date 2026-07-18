package com.romisspa.app.di

import android.content.Context
import com.romisspa.app.presentation.screens.reserva.ReservaViewModel
import com.romisspa.app.presentation.screens.servicios.ServiciosViewModel
import com.romisspa.app.presentation.screens.citas.CitasViewModel
import com.romisspa.app.presentation.screens.clientes.ClientesViewModel
import com.romisspa.app.presentation.screens.dashboard.DashboardViewModel
import com.romisspa.app.presentation.screens.ventas.VentasViewModel
import com.romisspa.app.presentation.screens.inventario.InventarioViewModel
import com.romisspa.app.presentation.screens.limpieza.LimpiezaViewModel
import com.romisspa.app.presentation.screens.empleados.EmpleadosViewModel
import com.romisspa.app.presentation.screens.reportes.ReportesViewModel
import com.romisspa.app.presentation.screens.caja.CajaViewModel

class ViewModelModule(
    private val useCaseModule: UseCaseModule,
    private val context: Context
) {

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

    fun provideDashboardViewModel(): DashboardViewModel {
        return DashboardViewModel(useCaseModule.spaUseCases)
    }

    fun provideVentasViewModel(): VentasViewModel {
        return VentasViewModel(useCaseModule.spaUseCases)
    }

    fun provideInventarioViewModel(): InventarioViewModel {
        return InventarioViewModel(useCaseModule.spaUseCases)
    }

    fun provideEmpleadosViewModel(): EmpleadosViewModel {
        return EmpleadosViewModel(useCaseModule.spaUseCases)
    }

    fun provideLimpiezaViewModel(): LimpiezaViewModel {
        return LimpiezaViewModel(useCases = useCaseModule.spaUseCases)
    }

    fun provideReportesViewModel(): ReportesViewModel {
        return ReportesViewModel(useCaseModule.spaUseCases)
    }

    fun provideCajaViewModel(): CajaViewModel {
        return CajaViewModel(useCaseModule.spaUseCases, context)
    }
}
