package com.romisspa.app.di

import com.romisspa.app.data.repository.InMemorySpaRepository
import com.romisspa.app.domain.repository.SpaRepository

class RepositoryModule {
    val spaRepository: SpaRepository by lazy {
        InMemorySpaRepository()
    }
}
