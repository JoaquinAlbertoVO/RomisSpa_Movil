package com.romisspa.app.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.romisspa.app.core.navigation.NavRoutes
import com.romisspa.app.ui.theme.Cream
import com.romisspa.app.ui.theme.RoseGold
import com.romisspa.app.ui.theme.GreyWarm

@Composable
fun AppBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // No mostrar BottomBar en el Login
    if (currentRoute == NavRoutes.LOGIN) return

    NavigationBar(
        containerColor = Cream
    ) {
        NavigationBarItem(
            selected = currentRoute == NavRoutes.DASHBOARD,
            onClick = {
                if (currentRoute != NavRoutes.DASHBOARD) {
                    navController.navigate(NavRoutes.DASHBOARD) {
                        popUpTo(NavRoutes.DASHBOARD) { inclusive = true }
                    }
                }
            },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoseGold,
                selectedTextColor = RoseGold,
                unselectedIconColor = GreyWarm,
                unselectedTextColor = GreyWarm,
                indicatorColor = Cream
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.CITAS,
            onClick = {
                if (currentRoute != NavRoutes.CITAS) {
                    navController.navigate(NavRoutes.CITAS)
                }
            },
            icon = { Icon(Icons.Default.CalendarMonth, null) },
            label = { Text("Citas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoseGold,
                selectedTextColor = RoseGold,
                unselectedIconColor = GreyWarm,
                unselectedTextColor = GreyWarm,
                indicatorColor = Cream
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.SERVICIOS,
            onClick = {
                if (currentRoute != NavRoutes.SERVICIOS) {
                    navController.navigate(NavRoutes.SERVICIOS)
                }
            },
            icon = { Icon(Icons.Default.Spa, null) },
            label = { Text("Servicios") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoseGold,
                selectedTextColor = RoseGold,
                unselectedIconColor = GreyWarm,
                unselectedTextColor = GreyWarm,
                indicatorColor = Cream
            )
        )
        NavigationBarItem(
            selected = currentRoute == NavRoutes.CLIENTES,
            onClick = {
                if (currentRoute != NavRoutes.CLIENTES) {
                    navController.navigate(NavRoutes.CLIENTES)
                }
            },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Clientes") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RoseGold,
                selectedTextColor = RoseGold,
                unselectedIconColor = GreyWarm,
                unselectedTextColor = GreyWarm,
                indicatorColor = Cream
            )
        )
    }
}
