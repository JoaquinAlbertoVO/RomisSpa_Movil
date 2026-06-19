package com.romisspa.app.di

import com.romisspa.app.data.repository.RetrofitSpaRepository
import com.romisspa.app.domain.repository.SpaRepository

class RepositoryModule(networkModule: NetworkModule) {
    val spaRepository: SpaRepository by lazy {
        // Ahora usamos RemoteDataSource siguiendo el patrón de myBook2
        RetrofitSpaRepository(networkModule.remoteDataSource)
    }
}
