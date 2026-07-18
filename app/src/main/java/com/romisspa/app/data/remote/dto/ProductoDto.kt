package com.romisspa.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Double,
    @SerializedName("categoria") val categoria: String
)

data class CreateProductoRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Double,
    val categoria: String
)

data class UpdateProductoRequest(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Double,
    val categoria: String
)
