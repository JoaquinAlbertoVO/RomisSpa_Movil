package com.romisspa.app.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.romisspa.app.domain.model.Cita
import com.romisspa.app.domain.model.Cliente
import com.romisspa.app.domain.model.Servicio
import com.romisspa.app.domain.repository.SpaRepository
import com.romisspa.app.ui.theme.WarmGold

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
        Cita("Ana García", "Manicure Completa", "Hoy", "10:00 AM", "Pendiente", WarmGold),
        Cita("Beatriz López", "Corte y Peinado", "Hoy", "02:00 PM", "Pendiente", WarmGold)
    )

    private val _clientes = mutableStateListOf(
        Cliente("Ana García", "987 654 321", "12 Oct 2023", 5),
        Cliente("Beatriz López", "912 345 678", "05 Nov 2023", 2),
        Cliente("Carla Méndez", "998 877 665", "20 Nov 2023", 8),
        Cliente("Diana Pérez", "944 556 677", "15 Nov 2023", 3),
        Cliente("Elena Rivas", "922 113 344", "01 Nov 2023", 12)
    )

    override fun getServicios(): List<Servicio> = _servicios
    override fun addServicio(servicio: Servicio) { _servicios.add(servicio) }
    override fun updateServicio(servicio: Servicio) {
        val index = _servicios.indexOfFirst { it.nombre == servicio.nombre }
        if (index != -1) _servicios[index] = servicio
    }

    override fun deleteServicio(servicio: Servicio) {
        _servicios.remove(servicio)
    }

    override fun getCitas(): List<Cita> = _citas
    override fun addCita(cita: Cita) { _citas.add(cita) }
    override fun deleteCita(cita: Cita) { _citas.remove(cita) }

    override fun getClientes(): List<Cliente> = _clientes
    override fun addOrUpdateCliente(cliente: Cliente) {
        val index = _clientes.indexOfFirst { it.nombre.equals(cliente.nombre, ignoreCase = true) }
        if (index != -1) {
            _clientes[index] = cliente
        } else {
            _clientes.add(cliente)
        }
    }
}
