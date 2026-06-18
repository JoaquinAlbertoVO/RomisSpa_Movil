package com.romisspa.app.core.navigation

object NavRoutes {
    const val LOGIN      = "login"
    const val DASHBOARD  = "dashboard"
    const val RESERVA    = "reserva"
    const val SERVICIOS  = "servicios"
    const val CLIENTES   = "clientes"
    const val CITAS      = "citas"

    fun getTitle(route: String?): String {
        return when (route) {
            LOGIN -> "Iniciar Sesión"
            DASHBOARD -> "Panel de Control"
            RESERVA -> "Nueva Reserva"
            SERVICIOS -> "Servicios"
            CLIENTES -> "Clientes"
            CITAS -> "Mis Citas"
            else -> "Romis Spa"
        }
    }
}
