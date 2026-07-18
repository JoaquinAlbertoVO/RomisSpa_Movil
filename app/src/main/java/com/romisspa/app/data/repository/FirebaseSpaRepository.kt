package com.romisspa.app.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.romisspa.app.data.mapper.*
import com.romisspa.app.domain.model.*
import com.romisspa.app.domain.repository.SpaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class FirebaseSpaRepository : SpaRepository {
    private val db = Firebase.firestore

    // ── SERVICIOS ─────────────────────────────────────────────────────────────

    override suspend fun getServicios(): List<Servicio> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("servicios").get().await()
        snapshot.documents.mapNotNull { doc ->
            val insumosList = (doc.get("insumos") as? List<*>)?.mapNotNull { raw ->
                val map = raw as? Map<*, *> ?: return@mapNotNull null
                val productoId = map["productoId"] as? String ?: return@mapNotNull null
                val nombre = map["nombre"] as? String ?: return@mapNotNull null
                val cantidad = (map["cantidad"] as? Number)?.toDouble() ?: 1.0
                Insumo(productoId = productoId, nombre = nombre, cantidad = cantidad)
            } ?: emptyList()

            Servicio(
                id = doc.id,
                nombre = doc.getString("nombre") ?: return@mapNotNull null,
                descripcion = doc.getString("descripcion") ?: "",
                precio = "S/ " + (doc.getDouble("precio") ?: 0.0),
                duracion = doc.getString("duracion") ?: "60 min",
                insumos = insumosList
            )
        }
    }

    override suspend fun addServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        val docRef = if (servicio.id.isEmpty())
            db.collection("servicios").document()
        else
            db.collection("servicios").document(servicio.id)

        docRef.set(servicio.toDto().copy(id = docRef.id)).await()
        Unit
    }

    override suspend fun updateServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        val id = servicio.id.ifEmpty { servicio.nombre }
        db.collection("servicios").document(id).set(servicio.toDto()).await()
        Unit
    }

    override suspend fun deleteServicio(servicio: Servicio) = withContext(Dispatchers.IO) {
        val id = servicio.id.ifEmpty { servicio.nombre }
        db.collection("servicios").document(id).delete().await()
        Unit
    }

    // ── CITAS ─────────────────────────────────────────────────────────────────

    override suspend fun getCitas(): List<Cita> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("citas").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.CitaDto(
                id = doc.id,
                cliente = doc.getString("cliente") ?: "",
                telefono = doc.getString("telefono"),
                servicio = doc.getString("servicio") ?: "",
                fecha = doc.getString("fecha") ?: "",
                hora = doc.getString("hora") ?: "",
                empleadoId = doc.getString("empleadoId"),
                estado = doc.getString("estado")
            ).toDomain()
        }
    }

    override suspend fun addCita(cita: Cita) = withContext(Dispatchers.IO) {
        val id = cita.id.ifEmpty { UUID.randomUUID().toString() }
        val citaConId = cita.copy(id = id)
        db.collection("citas").document(id).set(citaConId.toDto()).await()
        Unit
    }

    override suspend fun deleteCita(cita: Cita) = withContext(Dispatchers.IO) {
        db.collection("citas").document(cita.id).delete().await()
        Unit
    }

    override suspend fun updateCitaStatus(citaId: String, nuevoEstado: String) = withContext(Dispatchers.IO) {
        db.collection("citas").document(citaId).update("estado", nuevoEstado).await()
        Unit
    }

    // ── CLIENTES ──────────────────────────────────────────────────────────────

    override suspend fun getClientes(): List<Cliente> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("clientes").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.ClienteDto(
                nombre = doc.getString("nombre") ?: "",
                telefono = doc.getString("telefono") ?: doc.id,
                ultimaVisita = doc.getString("ultimaVisita"),
                totalVisitas = doc.getLong("totalVisitas")?.toInt()
            ).toDomain()
        }
    }

    override suspend fun addOrUpdateCliente(cliente: Cliente) = withContext(Dispatchers.IO) {
        // Usamos el teléfono como ID de documento para identificar clientes únicamente
        db.collection("clientes").document(cliente.telefono).set(cliente.toDto()).await()
        Unit
    }

    // ── VENTAS ────────────────────────────────────────────────────────────────

    override suspend fun getVentas(): List<Venta> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("ventas").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.VentaDto(
                id = doc.id,
                cliente = doc.getString("cliente") ?: "",
                servicio = doc.getString("servicio") ?: "",
                monto = doc.getDouble("monto") ?: 0.0,
                empleadoId = doc.getString("empleadoId"),
                metodoPago = doc.getString("metodoPago") ?: "Efectivo",
                fecha = doc.getString("fecha") ?: ""
            ).toDomain()
        }
    }

    override suspend fun addVenta(venta: Venta) = withContext(Dispatchers.IO) {
        val id = venta.id.ifEmpty { UUID.randomUUID().toString() }
        val ventaConId = venta.copy(id = id)
        db.collection("ventas").document(id).set(ventaConId.toDto()).await()
        Unit
    }

    // ── PRODUCTOS (INVENTARIO) ─────────────────────────────────────────────────

    override suspend fun getProductos(): List<Producto> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("productos").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.ProductoDto(
                id = doc.id,
                nombre = doc.getString("nombre") ?: "",
                descripcion = doc.getString("descripcion") ?: "",
                precio = doc.getDouble("precio") ?: 0.0,
                stock = doc.getDouble("stock") ?: 0.0,
                categoria = doc.getString("categoria") ?: ""
            ).toDomain()
        }
    }

    override suspend fun addProducto(producto: Producto) = withContext(Dispatchers.IO) {
        val id = producto.id.ifEmpty { UUID.randomUUID().toString() }
        val prodConId = producto.copy(id = id)
        db.collection("productos").document(id).set(prodConId.toDto()).await()
        Unit
    }

    override suspend fun updateProducto(producto: Producto) = withContext(Dispatchers.IO) {
        db.collection("productos").document(producto.id).set(producto.toDto()).await()
        Unit
    }

    override suspend fun deleteProducto(productoId: String) = withContext(Dispatchers.IO) {
        db.collection("productos").document(productoId).delete().await()
        Unit
    }

    /**
     * Descuenta del inventario los insumos usados en una cita.
     * Permite cantidades decimales (Double): 0.5, 1.5, etc.
     */
    override suspend fun descontarInsumos(insumosUsados: List<Insumo>) = withContext(Dispatchers.IO) {
        val batch = db.batch()
        insumosUsados.forEach { insumo ->
            if (insumo.productoId.isNotEmpty()) {
                val docRef = db.collection("productos").document(insumo.productoId)
                // Leemos el stock actual y descontamos
                val doc = docRef.get().await()
                val stockActual = doc.getDouble("stock") ?: 0.0
                val nuevoStock = maxOf(0.0, stockActual - insumo.cantidad)
                batch.update(docRef, "stock", nuevoStock)
            }
        }
        batch.commit().await()
        Unit
    }

    // ── EMPLEADOS ─────────────────────────────────────────────────────────────

    override suspend fun getEmpleados(): List<Empleado> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("empleados").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.EmpleadoDto(
                id = doc.id,
                nombre = doc.getString("nombre") ?: "",
                especialidad = doc.getString("especialidad") ?: "",
                telefono = doc.getString("telefono") ?: "",
                comision = doc.getDouble("comision") ?: 0.0
            ).toDomain()
        }
    }

    override suspend fun addEmpleado(empleado: Empleado) = withContext(Dispatchers.IO) {
        val id = empleado.id.ifEmpty { UUID.randomUUID().toString() }
        val empConId = empleado.copy(id = id)
        db.collection("empleados").document(id).set(empConId.toDto()).await()
        Unit
    }

    override suspend fun updateEmpleado(empleado: Empleado) = withContext(Dispatchers.IO) {
        db.collection("empleados").document(empleado.id).set(empleado.toDto()).await()
        Unit
    }

    override suspend fun deleteEmpleado(empleadoId: String) = withContext(Dispatchers.IO) {
        db.collection("empleados").document(empleadoId).delete().await()
        Unit
    }

    // ── LIMPIEZA ──────────────────────────────────────────────────────────────

    override suspend fun getRutinasDiarias(): List<RutinaDiaria> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("rutinas_diarias").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.RutinaDiariaDto(
                id = doc.id,
                tarea = doc.getString("tarea") ?: "",
                empleadoId = doc.getString("empleadoId") ?: "",
                empleadoNombre = doc.getString("empleadoNombre") ?: "",
                fechaActualizacion = doc.getString("fechaActualizacion") ?: "",
                estadoActual = doc.getString("estadoActual") ?: "PENDIENTE"
            ).toDomain()
        }
    }

    override suspend fun addOrUpdateRutinaDiaria(rutina: RutinaDiaria) = withContext(Dispatchers.IO) {
        val id = rutina.id.ifEmpty { UUID.randomUUID().toString() }
        val rutinaConId = rutina.copy(id = id)
        db.collection("rutinas_diarias").document(id).set(rutinaConId.toDto()).await()
        Unit
    }

    override suspend fun deleteRutinaDiaria(rutinaId: String) = withContext(Dispatchers.IO) {
        db.collection("rutinas_diarias").document(rutinaId).delete().await()
        Unit
    }

    override suspend fun getTareasEspeciales(): List<TareaEspecial> = withContext(Dispatchers.IO) {
        val snapshot = db.collection("tareas_especiales").get().await()
        snapshot.documents.mapNotNull { doc ->
            com.romisspa.app.data.remote.dto.TareaEspecialDto(
                id = doc.id,
                tarea = doc.getString("tarea") ?: "",
                empleadoId = doc.getString("empleadoId") ?: "",
                empleadoNombre = doc.getString("empleadoNombre") ?: "",
                fechaAsignada = doc.getString("fechaAsignada") ?: "",
                estado = doc.getString("estado") ?: "PENDIENTE"
            ).toDomain()
        }
    }

    override suspend fun addOrUpdateTareaEspecial(tarea: TareaEspecial) = withContext(Dispatchers.IO) {
        val id = tarea.id.ifEmpty { UUID.randomUUID().toString() }
        val tareaConId = tarea.copy(id = id)
        db.collection("tareas_especiales").document(id).set(tareaConId.toDto()).await()
        Unit
    }

    override suspend fun deleteTareaEspecial(tareaId: String) = withContext(Dispatchers.IO) {
        db.collection("tareas_especiales").document(tareaId).delete().await()
        Unit
    }
}
