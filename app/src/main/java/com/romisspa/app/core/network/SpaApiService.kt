package com.romisspa.app.core.network

import com.romisspa.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface SpaApiService {

    // SERVICIOS
    @GET("servicios")
    suspend fun getServicios(): Response<ApiResponse<List<ServicioDto>>>

    @POST("servicios")
    suspend fun addServicio(@Body request: CreateServicioRequest): Response<ApiResponse<Unit>>

    @PUT("servicios")
    suspend fun updateServicio(@Body request: UpdateServicioRequest): Response<ApiResponse<Unit>>

    @DELETE("servicios/{nombre}")
    suspend fun deleteServicio(@Path("nombre") nombre: String): Response<ApiResponse<Unit>>

    // CITAS
    @GET("citas")
    suspend fun getCitas(): Response<ApiResponse<List<CitaDto>>>

    @POST("citas")
    suspend fun addCita(@Body request: CreateCitaRequest): Response<ApiResponse<Unit>>

    @DELETE("citas/{id}")
    suspend fun deleteCita(@Path("id") id: String): Response<ApiResponse<Unit>>

    // CLIENTES
    @GET("clientes")
    suspend fun getClientes(): Response<ApiResponse<List<ClienteDto>>>

    @POST("clientes")
    suspend fun addOrUpdateCliente(@Body cliente: ClienteDto): Response<ApiResponse<Unit>>

    companion object {
        const val BASE_URL = "https://TU_API_GATEWAY_URL.amazonaws.com/prod/"
    }
}
