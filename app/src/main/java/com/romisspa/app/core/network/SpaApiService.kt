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

    @PATCH("citas/{id}")
    suspend fun updateCitaStatus(
        @Path("id") id: String,
        @Body request: UpdateCitaStatusRequest
    ): Response<ApiResponse<Unit>>

    // CLIENTES
    @GET("clientes")
    suspend fun getClientes(): Response<ApiResponse<List<ClienteDto>>>

    @POST("clientes")
    suspend fun addOrUpdateCliente(@Body cliente: ClienteDto): Response<ApiResponse<Unit>>

    // VENTAS
    @GET("ventas")
    suspend fun getVentas(): Response<ApiResponse<List<VentaDto>>>

    @POST("ventas")
    suspend fun addVenta(@Body request: CreateVentaRequest): Response<ApiResponse<VentaDto>>

    // PRODUCTOS
    @GET("productos")
    suspend fun getProductos(): Response<ApiResponse<List<ProductoDto>>>

    @POST("productos")
    suspend fun addProducto(@Body request: CreateProductoRequest): Response<ApiResponse<Unit>>

    @PUT("productos")
    suspend fun updateProducto(@Body request: UpdateProductoRequest): Response<ApiResponse<Unit>>

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: String): Response<ApiResponse<Unit>>

    // EMPLEADOS
    @GET("empleados")
    suspend fun getEmpleados(): Response<ApiResponse<List<EmpleadoDto>>>

    @POST("empleados")
    suspend fun addEmpleado(@Body request: CreateEmpleadoRequest): Response<ApiResponse<Unit>>

    @PUT("empleados")
    suspend fun updateEmpleado(@Body request: EmpleadoDto): Response<ApiResponse<Unit>>

    @DELETE("empleados/{id}")
    suspend fun deleteEmpleado(@Path("id") id: String): Response<ApiResponse<Unit>>

    companion object {
        const val BASE_URL = "https://zv9s3wu4oa.execute-api.us-east-1.amazonaws.com/"
    }
}
