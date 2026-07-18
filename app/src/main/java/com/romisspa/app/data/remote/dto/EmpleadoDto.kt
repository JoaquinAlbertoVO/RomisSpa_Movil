package com.romisspa.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class EmpleadoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("comision") val comision: Double
)

data class CreateEmpleadoRequest(
    val nombre: String,
    val especialidad: String,
    val telefono: String,
    val comision: Double
)
