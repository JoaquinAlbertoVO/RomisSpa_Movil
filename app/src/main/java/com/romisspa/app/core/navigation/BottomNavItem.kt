package com.romisspa.app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = NavRoutes.DASHBOARD,
        title = "Inicio",
        icon = Icons.Default.Home
    )

    data object Citas : BottomNavItem(
        route = NavRoutes.CITAS,
        title = "Citas",
        icon = Icons.Default.CalendarMonth
    )

    data object Servicios : BottomNavItem(
        route = NavRoutes.SERVICIOS,
        title = "Servicios",
        icon = Icons.Default.Spa
    )

    data object Clientes : BottomNavItem(
        route = NavRoutes.CLIENTES,
        title = "Clientes",
        icon = Icons.Default.Person
    )
}
