package com.casajojo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.casajojo.app.ui.screens.HomeScreen
import com.casajojo.app.ui.screens.LoginScreen
import com.casajojo.app.viewmodel.ClienteViewModel
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

//-- Registro e identificacion del cliente (RF19)
        composable("login") {

                val clienteViewModel: ClienteViewModel = viewModel()
            LoginScreen(
                clienteViewModel = clienteViewModel,
                onNavigateToCliente = { nombre, telefono ->
                    val nombreEncoded = URLEncoder.encode(nombre, StandardCharsets.UTF_8.toString())
                    navController.navigate("home_cliente/$nombreEncoded/$telefono")
                },
                onNavigateToCaja = {
                    // Ruta para caja cuando la creen
                },
                onNavigateToAdmin = {
                    // Ruta para admin cuando la creen
                }
            )
        }
        //MENU DEL CLIENTE (HOME)
        composable(
            route = "home_cliente/{nombre}/{telefono}",
            arguments = listOf(
                navArgument("nombre"){type= NavType.StringType },
                navArgument("telefono"){type= NavType.StringType}
            )
        ) { backStackEntry ->
            // Decodificamos los datos recibidos
            val nombreRaw = backStackEntry.arguments?.getString("nombre") ?: ""
            val nombreDecoded = URLDecoder.decode(nombreRaw, StandardCharsets.UTF_8.toString())
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""

            // Carga la pantalla principal del cliente pasando sus datos
            HomeScreen(
                nombreCliente = nombreDecoded,
                telefonoCliente = telefono,
                onVerCarritoClick = {
                    navController.navigate("carrito")
                }
            )
        }

        // 3. PANTALLAS SECUNDARIAS (Rutas simples)
        composable("carrito") {
            // Reemplazarás esto cuando crees CarritoScreen()
        }

        composable("caja") {
            // Reemplazarás esto cuando crees CajaScreen()
        }

        composable("admin") {
            // Reemplazarás esto cuando crees AdminScreen()
        }
    }
}