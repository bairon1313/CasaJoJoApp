package com.casajojo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.casajojo.app.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToCliente = { navController.navigate(Screen.Home.route) },
                onNavigateToCaja = { navController.navigate(Screen.Caja.route) },
                onNavigateToAdmin = { navController.navigate(Screen.Admin.route) }
            )
        }
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
            CajaScreen()
        }
        composable(Screen.Admin.route) {
            AdminScreen()
        }
    }
}