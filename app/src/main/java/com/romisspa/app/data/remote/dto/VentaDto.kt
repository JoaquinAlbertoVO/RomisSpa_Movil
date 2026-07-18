package com.romisspa.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VentaDto(
    @SerializedName("id") val id: String,
    @SerializedName("cliente") val cliente: String,
    @SerializedName("servicio") val servicio: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("empleadoId") val empleadoId: String?,
    @SerializedName("metodoPago") val metodoPago: String,
    @SerializedName("fecha") val fecha: String
)
