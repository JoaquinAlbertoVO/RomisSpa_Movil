package com.romisspa.app.data.remote.datasource

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.romisspa.app.data.remote.dto.*
import kotlinx.coroutines.tasks.await

class RemoteDataSource {
    private val db = Firebase.firestore

    private fun <T> createSuccessResponse(data: T): ApiResponse<T> = ApiResponse(success = true, data = data)

    suspend fun getServicios(): ApiResponse<List<ServicioDto>>? {
        val snapshot = db.collection("servicios").get().await()
        val list = snapshot.documents.map { doc -> 
            ServicioDto(
                nombre = doc.getString("nombre") ?: "",
                descripcion = doc.getString("descripcion") ?: "",
                precio = doc.getDouble("precio") ?: 0.0,
                imagenRes = doc.getString("imagenRes")
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addServicio(request: CreateServicioRequest) {
        db.collection("servicios").document(request.nombre).set(request).await()
    }

    suspend fun updateServicio(request: UpdateServicioRequest) {
        db.collection("servicios").document(request.nombre).set(request).await()
    }

    suspend fun deleteServicio(nombre: String) {
        db.collection("servicios").document(nombre).delete().await()
    }

    suspend fun getCitas(): ApiResponse<List<CitaDto>>? {
        val snapshot = db.collection("citas").get().await()
        val list = snapshot.documents.map { doc -> 
            CitaDto(
                id = doc.id,
                cliente = doc.getString("cliente") ?: "",
                telefono = doc.getString("telefono"),
                servicio = doc.getString("servicio") ?: "",
                fecha = doc.getString("fecha") ?: "",
                hora = doc.getString("hora") ?: "",
                empleadoId = doc.getString("empleadoId"),
                estado = doc.getString("estado")
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addCita(request: CreateCitaRequest) {
        db.collection("citas").add(request).await()
    }

    suspend fun deleteCita(id: String) {
        db.collection("citas").document(id).delete().await()
    }

    suspend fun updateCitaStatus(id: String, request: UpdateCitaStatusRequest) {
        db.collection("citas").document(id).update("estado", request.estado).await()
    }

    suspend fun getClientes(): ApiResponse<List<ClienteDto>>? {
        val snapshot = db.collection("clientes").get().await()
        val list = snapshot.documents.map { doc -> 
            ClienteDto(
                nombre = doc.getString("nombre") ?: "",
                telefono = doc.getString("telefono") ?: "",
                ultimaVisita = doc.getString("ultimaVisita"),
                totalVisitas = doc.getLong("totalVisitas")?.toInt()
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addOrUpdateCliente(cliente: ClienteDto) {
        // Usamos el telefono como ID del documento
        db.collection("clientes").document(cliente.telefono).set(cliente).await()
    }

    suspend fun getVentas(): ApiResponse<List<VentaDto>>? {
        val snapshot = db.collection("ventas").get().await()
        val list = snapshot.documents.map { doc -> 
            VentaDto(
                id = doc.id,
                cliente = doc.getString("cliente") ?: "",
                servicio = doc.getString("servicio") ?: "",
                monto = doc.getDouble("monto") ?: 0.0,
                empleadoId = doc.getString("empleadoId"),
                metodoPago = doc.getString("metodoPago") ?: "",
                fecha = doc.getString("fecha") ?: ""
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addVenta(request: CreateVentaRequest) {
        db.collection("ventas").add(request).await()
    }

    suspend fun getProductos(): ApiResponse<List<ProductoDto>>? {
        val snapshot = db.collection("productos").get().await()
        val list = snapshot.documents.map { doc -> 
            ProductoDto(
                id = doc.id,
                nombre = doc.getString("nombre") ?: "",
                descripcion = doc.getString("descripcion") ?: "",
                precio = doc.getDouble("precio") ?: 0.0,
                stock = doc.getDouble("stock") ?: 0.0,
                categoria = doc.getString("categoria") ?: ""
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addProducto(request: CreateProductoRequest) {
        db.collection("productos").add(request).await()
    }

    suspend fun updateProducto(request: UpdateProductoRequest) {
        db.collection("productos").document(request.id).set(request).await()
    }

    suspend fun deleteProducto(id: String) {
        db.collection("productos").document(id).delete().await()
    }

    suspend fun getEmpleados(): ApiResponse<List<EmpleadoDto>>? {
        val snapshot = db.collection("empleados").get().await()
        val list = snapshot.documents.map { doc -> 
            EmpleadoDto(
                id = doc.id,
                nombre = doc.getString("nombre") ?: "",
                especialidad = doc.getString("especialidad") ?: "",
                telefono = doc.getString("telefono") ?: "",
                comision = doc.getDouble("comision") ?: 0.0
            )
        }
        return createSuccessResponse(list)
    }

    suspend fun addEmpleado(request: CreateEmpleadoRequest) {
        db.collection("empleados").add(request).await()
    }

    suspend fun updateEmpleado(empleado: EmpleadoDto) {
        if (empleado.id != null) {
            db.collection("empleados").document(empleado.id).set(empleado).await()
        }
    }

    suspend fun deleteEmpleado(id: String) {
        db.collection("empleados").document(id).delete().await()
    }
}
