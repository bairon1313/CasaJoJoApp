package com.casajojo.app.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onNavigateToCliente: () -> Unit,
    onNavigateToCaja: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Casa JoJo SpA",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToCliente,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cliente (Realizar Pedido)")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateToCaja,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Operador de Caja")
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onNavigateToAdmin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Administrador")
        }
    }
}