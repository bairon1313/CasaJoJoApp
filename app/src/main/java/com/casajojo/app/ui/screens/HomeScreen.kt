package com.casajojo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.casajojo.app.model.PlatoMock
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onVerCarritoClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menú Casa JoJo") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onVerCarritoClick
            ) {
                Text(text = "Ver Pedido")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Catálogo de Platos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Ahora lee directo desde el objeto PlatoMock
                items(PlatoMock.listaPlatos) { plato ->
                    PlatoItem(
                        plato = plato,
                        onAgregarClick = { platoSeleccionado ->
                            // Se conectará al ViewModel más adelante
                        }
                    )
                }
            }
        }
    }
}