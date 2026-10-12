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

import com.casajojo.app.viewmodel.ClienteViewModel
import com.casajojo.app.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    clienteViewModel: ClienteViewModel,
    menuViewModel: MenuViewModel,
    onVolverClick: () -> Unit) {

//-- Estados reactivos en tiempo real
    val estadoCliente by clienteViewModel.estado.collectAsState()
    val estadoMenu by menuViewModel.estado.collectAsState()

//--Variable local solo para la hora de retiro (se podría mover al ViewModel más adelante para RF04)
    var horaRetiro by remember { mutableStateOf("") }

//--Consolidacion dinamica de platos seleccionados eliminando los nulos
    val listaPedido = listOfNotNull(
        estadoMenu.platoFondo1,
        estadoMenu.platoFondo2,
        estadoMenu.acompanamiento1,
        estadoMenu.acompanamiento2,
        estadoMenu.ensalada
    )

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
                        val precioMostrar = if (plato.categoria == "ENSALADA" && estadoMenu.porcionDobleEnsalada) plato.precio * 2 else plato.precio
                        Text(
                            text = "$${precioMostrar}",
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
                    text = "$${estadoMenu.precioTotal}",
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
                value = estadoCliente.nombre,
                onValueChange = { clienteViewModel.onNombreChange(it) },
                label = { Text("Nombre de quien retira") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = estadoCliente.telefono,
                onValueChange = { clienteViewModel.onTelefonoChange(it) },
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