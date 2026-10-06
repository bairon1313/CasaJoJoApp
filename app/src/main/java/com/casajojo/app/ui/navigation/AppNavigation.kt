package com.casajojo.app.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casajojo.app.ui.screens.CarritoScreen
import com.casajojo.app.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onVerCarritoClick = { navController.navigate(Screen.Carrito.route) }
            )
        }
        composable(Screen.Carrito.route) {
            CarritoScreen(
                onVolverClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Caja.route) {
            Text(text = "Pantalla Caja / Administración")
        }
    }
}