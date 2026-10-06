package com.casajojo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.casajojo.app.ui.components.PlatoMock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onVolverClick: () -> Unit
) {
    // Variables de estado para los datos del cliente y retiro
    var nombreCliente by remember { mutableStateOf("") }
    var telefonoContacto by remember { mutableStateOf("") }
    var horaRetiro by remember { mutableStateOf("") }

    // Platos de ejemplo tomados de PlatoMock
    val listaPedido = remember { PlatoMock.listaPlatos.take(2) }
    val totalPedido = remember { listaPedido.sumOf { it.precio } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del Pedido") },
                navigationIcon = {
                    TextButton(onClick = onVolverClick) {
                        Text(
                            text = "← Volver",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Detalle de Productos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Lista de platos seleccionados
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listaPedido) { plato ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = plato.nombre,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = plato.categoria,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "$${plato.precio}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }

            // Fila con el cálculo del total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total a Pagar:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$$totalPedido",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider()

            Text(
                text = "Datos para el Retiro en Local",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            // Campos para datos del cliente y hora de retiro
            OutlinedTextField(
                value = nombreCliente,
                onValueChange = { nombreCliente = it },
                label = { Text("Nombre de quien retira") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telefonoContacto,
                onValueChange = { telefonoContacto = it },
                label = { Text("Teléfono de contacto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = horaRetiro,
                onValueChange = { horaRetiro = it },
                label = { Text("Hora estimada de retiro (ej. 14:30)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de acción principal
            Button(
                onClick = { /* Se conectará con el ViewModel y Room DB */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Confirmar Pedido")
            }
        }
    }
}