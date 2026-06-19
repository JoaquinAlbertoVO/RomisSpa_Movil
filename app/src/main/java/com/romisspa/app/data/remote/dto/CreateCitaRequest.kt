package com.romisspa.app.data.remote.dto

data class CreateCitaRequest(
    val cliente: String,
    val servicio: String,
    val fecha: String,
    val hora: String
)
