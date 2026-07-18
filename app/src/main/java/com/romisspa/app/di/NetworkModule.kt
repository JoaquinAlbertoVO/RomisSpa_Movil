package com.romisspa.app.di

import com.romisspa.app.core.network.RetrofitClient
import com.romisspa.app.core.network.SpaApiService
import com.romisspa.app.data.remote.datasource.RemoteDataSource

class NetworkModule {
    private val apiService: SpaApiService by lazy {
        RetrofitClient.api
    }

    val remoteDataSource: RemoteDataSource by lazy {
        RemoteDataSource()
    }
}
