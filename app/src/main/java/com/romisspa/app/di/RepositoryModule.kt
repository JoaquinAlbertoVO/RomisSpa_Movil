package com.romisspa.app.di

import com.romisspa.app.data.repository.RetrofitSpaRepository
import com.romisspa.app.domain.repository.SpaRepository

class RepositoryModule(networkModule: NetworkModule) {
    val spaRepository: SpaRepository by lazy {
        // Cambiado de InMemorySpaRepository a RetrofitSpaRepository para producción
        RetrofitSpaRepository(networkModule.apiService)
    }
}
