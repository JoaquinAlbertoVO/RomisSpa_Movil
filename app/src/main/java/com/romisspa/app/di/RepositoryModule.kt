package com.romisspa.app.di

import android.content.Context
import com.romisspa.app.data.local.database.SpaDatabase
import com.romisspa.app.data.repository.RoomSpaRepository
import com.romisspa.app.domain.repository.SpaRepository

class RepositoryModule(context: Context, networkModule: NetworkModule) {
    private val database: SpaDatabase by lazy {
        SpaDatabase.getDatabase(context)
    }

    val spaRepository: SpaRepository by lazy {
        com.romisspa.app.data.repository.FirebaseSpaRepository()
    }
}
