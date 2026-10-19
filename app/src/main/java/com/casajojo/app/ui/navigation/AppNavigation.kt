package com.casajojo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.casajojo.app.ui.screens.admin.AdminScreen
import com.casajojo.app.ui.screens.auth.LoginScreen
import com.casajojo.app.ui.screens.cliente.CarritoScreen
import com.casajojo.app.ui.screens.cliente.HomeScreen
import com.casajojo.app.ui.screens.operador.CajaScreen
import com.casajojo.app.viewmodel.ClienteMenuViewModel
import com.casajojo.app.viewmodel.ClienteViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Instancias compartidas para que HomeScreen y CarritoScreen lean la misma comanda
    val clienteViewModel: ClienteViewModel = viewModel()
    val clienteMenuViewModel: ClienteMenuViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToCliente = { nombre, telefono ->
                    // Codificamos el nombre para soportar espacios y tildes
                    val nombreEncoded = URLEncoder.encode(nombre, StandardCharsets.UTF_8.toString())
                    navController.navigate("home_cliente/$nombreEncoded/$telefono")
                },
                onNavigateToCaja = {
                    navController.navigate("caja")
                },
                onNavigateToAdmin = {
                    navController.navigate("admin")
                }
            )
        }

        // MENÚ DEL CLIENTE (HOME)
        composable(
            route = "home_cliente/{nombre}/{telefono}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Decodificamos los datos recibidos
            val nombreRaw = backStackEntry.arguments?.getString("nombre") ?: ""
            val nombreDecoded = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""

            // Sincronizamos los datos del cliente ingresados con su ViewModel
            clienteViewModel.onNombreChange(nombreDecoded)
            clienteViewModel.onTelefonoChange(telefono)

            HomeScreen(
                nombreCliente = nombreDecoded,
                telefonoCliente = telefono,
                onVerCarritoClick = {
                    navController.navigate("carrito")
                },
                menuViewModel = clienteMenuViewModel
            )
        }

        composable("carrito") {
            CarritoScreen(
                clienteViewModel = clienteViewModel,
                menuViewModel = clienteMenuViewModel,
                onVolverClick = { navController.popBackStack() }
            )
        }

        composable("caja") {
            CajaScreen()
        }

        composable("admin") {
            AdminScreen(viewModel = viewModel())
        }
    }
}