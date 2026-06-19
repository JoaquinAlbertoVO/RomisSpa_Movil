package com.romisspa.app.di

class AppContainer {
    private val networkModule = NetworkModule()
    private val repositoryModule = RepositoryModule(networkModule)
    private val useCaseModule = UseCaseModule(repositoryModule)
    
    // Agregamos ViewModelModule siguiendo la estructura de myBook2
    val viewModelModule = ViewModelModule(useCaseModule)

    // Los ViewModels ahora se obtienen a través del ViewModelModule
    val reservaViewModel by lazy { viewModelModule.provideReservaViewModel() }
    val serviciosViewModel by lazy { viewModelModule.provideServiciosViewModel() }
    val citasViewModel by lazy { viewModelModule.provideCitasViewModel() }
    val clientesViewModel by lazy { viewModelModule.provideClientesViewModel() }
}
