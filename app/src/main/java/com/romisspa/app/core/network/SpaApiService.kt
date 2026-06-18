package com.romisspa.app.core.network

import com.romisspa.app.data.remote.dto.CitaDto
import com.romisspa.app.data.remote.dto.ClienteDto
import com.romisspa.app.data.remote.dto.ServicioDto
import retrofit2.http.*

interface SpaApiService {

    // SERVICIOS
    @GET("servicios")
    suspend fun getServicios(): List<ServicioDto>

    @POST("servicios")
    suspend fun addServicio(@Body servicio: ServicioDto)

    @PUT("servicios")
    suspend fun updateServicio(@Body servicio: ServicioDto)

    @DELETE("servicios/{nombre}")
    suspend fun deleteServicio(@Path("nombre") nombre: String)

    // CITAS
    @GET("citas")
    suspend fun getCitas(): List<CitaDto>

    @POST("citas")
    suspend fun addCita(@Body cita: CitaDto)

    @DELETE("citas/{id}")
    suspend fun deleteCita(@Path("id") id: String)

    // CLIENTES
    @GET("clientes")
    suspend fun getClientes(): List<ClienteDto>

    @POST("clientes")
    suspend fun addOrUpdateCliente(@Body cliente: ClienteDto)

    companion object {
        // Tu compañera deberá reemplazar esta URL por la de API Gateway
        const val BASE_URL = "https://TU_API_GATEWAY_URL.amazonaws.com/prod/"
    }
}
