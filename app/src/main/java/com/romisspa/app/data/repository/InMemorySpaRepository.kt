package com.romisspa.app.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.romisspa.app.domain.model.*
import com.romisspa.app.domain.repository.SpaRepository
import com.romisspa.app.ui.theme.WarmGold
import kotlinx.coroutines.delay
import java.util.UUID

class InMemorySpaRepository : SpaRepository {
    private val _servicios = mutableStateListOf(
        Servicio(nombre = "Corte y Peinado", descripcion = "Corte dama/caballero + peinado express", precio = "S/ 45.00", duracion = "45 min"),
        Servicio(nombre = "Manicure Completa", descripcion = "Limpieza, limado y esmaltado en gel", precio = "S/ 35.00", duracion = "60 min"),
        Servicio(nombre = "Limpieza Facial", descripcion = "Hidratación profunda con vapor de ozono", precio = "S/ 80.00", duracion = "50 min"),
        Servicio(nombre = "Masaje Relajante", descripcion = "Masaje de cuerpo completo con aceites", precio = "S/ 120.00", duracion = "60 min"),
        Servicio(nombre = "Tinte y Color", descripcion = "Coloración completa con productos premium", precio = "S/ 150.00", duracion = "120 min"),
        Servicio(nombre = "Pedicure Spa", descripcion = "Exfoliación, masaje y esmaltado", precio = "S/ 40.00", duracion = "60 min")
    )

    private val _citas = mutableStateListOf(
        Cita(id = UUID.randomUUID().toString(), cliente = "Ana García", servicio = "Manicure Completa", fecha = "Hoy", hora = "10:00 AM", estado = "Pendiente", colorEstado = WarmGold),
        Cita(id = UUID.randomUUID().toString(), cliente = "Beatriz López", servicio = "Corte y Peinado", fecha = "Hoy", hora = "02:00 PM", estado = "Pendiente", colorEstado = WarmGold)
    )

    private val _clientes = mutableStateListOf(
        Cliente("Ana García", "987 654 321", "12 Oct 2023", 5),
        Cliente("Beatriz López", "912 345 678", "05 Nov 2023", 2),
        Cliente("Carla Méndez", "998 877 665", "20 Nov 2023", 8),
        Cliente("Diana Pérez", "944 556 677", "15 Nov 2023", 3),
        Cliente("Elena Rivas", "922 113 344", "01 Nov 2023", 12)
    )

    private val _ventas = mutableStateListOf<Venta>()
    private val _productos = mutableStateListOf<Producto>()
    private val _empleados = mutableStateListOf<Empleado>()

    override suspend fun getServicios(): List<Servicio> {
        delay(500)
        return _servicios
    }
    
    override suspend fun addServicio(servicio: Servicio) { 
        delay(500)
        _servicios.add(servicio) 
    }
    
    override suspend fun updateServicio(servicio: Servicio) {
        delay(500)
        val index = _servicios.indexOfFirst { it.id == servicio.id }
        if (index != -1) _servicios[index] = servicio
    }

    override suspend fun deleteServicio(servicio: Servicio) {
        delay(500)
        _servicios.remove(servicio)
    }

    override suspend fun getCitas(): List<Cita> {
        delay(500)
        return _citas
    }
    
    override suspend fun addCita(cita: Cita) { 
        delay(500)
        val citaConId = if (cita.id.isEmpty()) cita.copy(id = UUID.randomUUID().toString()) else cita
        _citas.add(citaConId) 
    }
    
    override suspend fun deleteCita(cita: Cita) { 
        delay(500)
        _citas.removeAll { it.id == cita.id }
    }

    override suspend fun updateCitaStatus(citaId: String, nuevoEstado: String) {
        delay(500)
        val index = _citas.indexOfFirst { it.id == citaId }
        if (index != -1) {
            _citas[index] = _citas[index].copy(estado = nuevoEstado)
        }
    }

    override suspend fun getClientes(): List<Cliente> {
        delay(500)
        return _clientes
    }
    
    override suspend fun addOrUpdateCliente(cliente: Cliente) {
        delay(500)
        val index = _clientes.indexOfFirst { it.telefono == cliente.telefono }
        if (index != -1) {
            _clientes[index] = cliente
        } else {
            _clientes.add(cliente)
        }
    }

    override suspend fun getVentas(): List<Venta> {
        delay(500)
        return _ventas
    }

    override suspend fun addVenta(venta: Venta) {
        delay(500)
        _ventas.add(venta.copy(id = UUID.randomUUID().toString()))
    }

    override suspend fun getProductos(): List<Producto> {
        delay(500)
        return _productos
    }

    override suspend fun addProducto(producto: Producto) {
        delay(500)
        _productos.add(producto.copy(id = UUID.randomUUID().toString()))
    }

    override suspend fun updateProducto(producto: Producto) {
        delay(500)
        val index = _productos.indexOfFirst { it.id == producto.id }
        if (index != -1) _productos[index] = producto
    }

    override suspend fun deleteProducto(productoId: String) {
        delay(500)
        _productos.removeAll { it.id == productoId }
    }

    override suspend fun getEmpleados(): List<Empleado> {
        delay(500)
        return _empleados
    }

    override suspend fun addEmpleado(empleado: Empleado) {
        delay(500)
        _empleados.add(empleado.copy(id = UUID.randomUUID().toString()))
    }

    override suspend fun updateEmpleado(empleado: Empleado) {
        delay(500)
        val index = _empleados.indexOfFirst { it.id == empleado.id }
        if (index != -1) _empleados[index] = empleado
    }

    override suspend fun deleteEmpleado(empleadoId: String) {
        delay(500)
        _empleados.removeAll { it.id == empleadoId }
    }

    override suspend fun descontarInsumos(insumosUsados: List<Insumo>) {
        delay(500)
        insumosUsados.forEach { insumo ->
            val index = _productos.indexOfFirst { it.id == insumo.productoId }
            if (index != -1) {
                val prod = _productos[index]
                _productos[index] = prod.copy(stock = maxOf(0.0, prod.stock - insumo.cantidad))
            }
        }
    }

    override suspend fun getRutinasDiarias(): List<RutinaDiaria> = emptyList()
    override suspend fun addOrUpdateRutinaDiaria(rutina: RutinaDiaria) {}
    override suspend fun deleteRutinaDiaria(rutinaId: String) {}

    override suspend fun getTareasEspeciales(): List<TareaEspecial> = emptyList()
    override suspend fun addOrUpdateTareaEspecial(tarea: TareaEspecial) {}
    override suspend fun deleteTareaEspecial(tareaId: String) {}
}
