package com.casajojo.app.ui.screens.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casajojo.app.model.MenuUiState
import com.casajojo.app.model.Plato
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.ClienteMenuViewModel
import com.casajojo.app.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    clienteViewModel: ClienteViewModel,
    menuViewModel: ClienteMenuViewModel,
    onVolverClick: () -> Unit
) {
    //-- Estados reactivos en tiempo real
    val estadoCliente by clienteViewModel.estado.collectAsState()
    val estadoMenu by menuViewModel.estado.collectAsState()

    //-- Variable local para la hora de retiro
    var horaRetiro by remember { mutableStateOf("") }

    //-- Consolidación dinámica de platos seleccionados eliminando nulos
    val listaPedido = listOfNotNull(
        estadoMenu.platoFondo1,
        estadoMenu.platoFondo2,
        estadoMenu.acompanamiento1,
        estadoMenu.acompanamiento2,
        estadoMenu.ensalada
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resumen del Pedido",
                        color = YellowLogo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = YellowLogo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sección 1: Detalle del pedido en Tarjeta estilizada
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Detalle de Productos",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (listaPedido.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No has seleccionado ningún plato aún.",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(listaPedido) { plato ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = plato.nombre,
                                            color = TextWhite,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = plato.categoria,
                                            color = YellowLogo,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    val precioMostrar = if (plato.categoria == "ENSALADA" && estadoMenu.porcionDobleEnsalada) {
                                        plato.precio * 2
                                    } else {
                                        plato.precio
                                    }

                                    Text(
                                        text = "$${precioMostrar}",
                                        color = YellowLogo,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 10.dp),
                                    color = TextMuted.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Total a pagar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a Pagar:",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$${estadoMenu.precioTotal}",
                            color = YellowLogo,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Sección 2: Datos para el Retiro en Local
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Datos para el Retiro en Local",
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Nombre de quien retira
                    OutlinedTextField(
                        value = estadoCliente.nombre,
                        onValueChange = { clienteViewModel.onNombreChange(it) },
                        label = { Text("Nombre de quien retira", color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = YellowLogo,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = TextWhite, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YellowLogo,
                            unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                            focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f)
                        )
                    )

                    // Teléfono de contacto
                    OutlinedTextField(
                        value = estadoCliente.telefono,
                        onValueChange = { clienteViewModel.onTelefonoChange(it) },
                        label = { Text("Teléfono de contacto", color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = YellowLogo,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = TextWhite, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YellowLogo,
                            unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                            focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f)
                        )
                    )

                    // Hora estimada de retiro
                    OutlinedTextField(
                        value = horaRetiro,
                        onValueChange = { horaRetiro = it },
                        label = { Text("Hora estimada de retiro (ej. 14:30)", color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = YellowLogo,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = TextWhite, fontSize = 14.sp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YellowLogo,
                            unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                            focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f)
                        )
                    )
                }
            }

            // Botón Confirmar Pedido
            Button(
                onClick = { /* Se conectará con el ViewModel y Room DB */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowLogo,
                    contentColor = DarkBackground
                )
            ) {
                Text(
                    text = "Confirmar Pedido",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}