package com.romisspa.app.core.navigation

object NavRoutes {
    const val LOGIN      = "login"
    const val DASHBOARD  = "dashboard"
    const val RESERVA    = "reserva"
    const val SERVICIOS  = "servicios"
    const val CLIENTES   = "clientes"
    const val CITAS      = "citas"
    const val VENTAS     = "ventas"
    const val INVENTARIO = "inventario"
    const val EMPLEADOS  = "empleados"
    const val LIMPIEZA   = "limpieza"
    const val REPORTES   = "reportes"
    const val CAJA       = "caja"

    fun getTitle(route: String?): String {
        return when (route) {
            LOGIN -> "Iniciar Sesión"
            DASHBOARD -> "Panel de Control"
            RESERVA -> "Nueva Reserva"
            SERVICIOS -> "Servicios"
            CLIENTES -> "Clientes"
            CITAS -> "Mis Citas"
            VENTAS -> "Caja y Ventas"
            INVENTARIO -> "Inventario"
            EMPLEADOS -> "Empleados"
            LIMPIEZA -> "Limpieza"
            REPORTES -> "Reportes y Analítica"
            CAJA -> "Flujo de Caja"
            else -> "Romis Spa"
        }
    }
}
