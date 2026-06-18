package com.romisspa.app.di

import com.romisspa.app.core.network.SpaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {
    val apiService: SpaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SpaApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpaApiService::class.java)
    }
}
