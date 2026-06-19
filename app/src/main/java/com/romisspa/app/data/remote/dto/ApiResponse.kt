package com.romisspa.app.data.remote.dto

data class ApiResponse<T>(
    val success: Boolean,
    val data: T,
    val message: String? = null
)
