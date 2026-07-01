package com.romisspa.app.di

import com.romisspa.app.data.repository.InMemorySpaRepository
import com.romisspa.app.data.repository.RetrofitSpaRepository
import com.romisspa.app.domain.repository.SpaRepository

class RepositoryModule(networkModule: NetworkModule) {
    val spaRepository: SpaRepository by lazy {
        // Cambiamos temporalmente a InMemory para probar la UI inmediatamente
        //InMemorySpaRepository()
        RetrofitSpaRepository(networkModule.remoteDataSource)
    }
}
