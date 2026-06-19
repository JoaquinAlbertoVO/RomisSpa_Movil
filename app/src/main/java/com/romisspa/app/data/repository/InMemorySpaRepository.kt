package com.romisspa.app.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository
import com.romisspa.app.ui.theme.WarmGold
import kotlinx.coroutines.delay
import java.util.UUID

class InMemorySpaRepository : SpaRepository {
    private val _servicios = mutableStateListOf(
        Servicio("Corte y Peinado", "Corte dama/caballero + peinado express", "S/ 45.00", "45 min"),
        Servicio("Manicure Completa", "Limpieza, limado y esmaltado en gel", "S/ 35.00", "60 min"),
        Servicio("Limpieza Facial", "Hidratación profunda con vapor de ozono", "S/ 80.00", "50 min"),
        Servicio("Masaje Relajante", "Masaje de cuerpo completo con aceites", "S/ 120.00", "60 min"),
        Servicio("Tinte y Color", "Coloración completa con productos premium", "S/ 150.00", "120 min"),
        Servicio("Pedicure Spa", "Exfoliación, masaje y esmaltado", "S/ 40.00", "60 min")
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
        val index = _servicios.indexOfFirst { it.nombre == servicio.nombre }
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

    override suspend fun getClientes(): List<Cliente> {
        delay(500)
        return _clientes
    }
    
    override suspend fun addOrUpdateCliente(cliente: Cliente) {
        delay(500)
        val index = _clientes.indexOfFirst { it.nombre.equals(cliente.nombre, ignoreCase = true) }
        if (index != -1) {
            _clientes[index] = cliente
        } else {
            _clientes.add(cliente)
        }
    }
}
