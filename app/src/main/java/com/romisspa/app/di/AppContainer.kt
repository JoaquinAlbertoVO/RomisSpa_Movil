package com.romisspa.app.di

import android.content.Context

class AppContainer(private val context: Context) {
    private val networkModule = NetworkModule()
    private val repositoryModule = RepositoryModule(context, networkModule)
    private val useCaseModule = UseCaseModule(repositoryModule)
    
    // Agregamos ViewModelModule siguiendo la estructura de myBook2
    val viewModelModule = ViewModelModule(useCaseModule, context)

    // Los ViewModels ahora se obtienen a través del ViewModelModule
    val reservaViewModel by lazy { viewModelModule.provideReservaViewModel() }
    val serviciosViewModel by lazy { viewModelModule.provideServiciosViewModel() }
    val citasViewModel by lazy { viewModelModule.provideCitasViewModel() }
    val clientesViewModel by lazy { viewModelModule.provideClientesViewModel() }
    val dashboardViewModel by lazy { viewModelModule.provideDashboardViewModel() }
    val ventasViewModel by lazy { viewModelModule.provideVentasViewModel() }
    val inventarioViewModel by lazy { viewModelModule.provideInventarioViewModel() }
    val empleadosViewModel by lazy { viewModelModule.provideEmpleadosViewModel() }
    val limpiezaViewModel by lazy { viewModelModule.provideLimpiezaViewModel() }
    val reportesViewModel by lazy { viewModelModule.provideReportesViewModel() }
    val cajaViewModel by lazy { viewModelModule.provideCajaViewModel() }
}
