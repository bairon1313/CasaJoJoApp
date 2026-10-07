package com.casajojo.app.ui.screens
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casajojo.app.R
import com.casajojo.app.ui.theme.*
import androidx.compose.ui.text.TextStyle

@Composable
fun LoginScreen(
    onNavigateToCliente: (nombre:String,telefono:String) -> Unit,
    onNavigateToCaja: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    // ESTADOS PARA EL MODAL Y VALIDACIÓN
    var showClienteDialog by remember { mutableStateOf(false) }
    var nombreCliente by remember { mutableStateOf("") }
    var telefonoCliente by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Insignia circular que recorta el PNG cuadrado eliminando bordes y esquinas
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo La Casa De JoJo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título principal con branding naranja y centrado
            Text(
                text = "Bienvenido a\nLa Casa Jo-Jo",
                style = MaterialTheme.typography.headlineMedium.copy(
                    lineHeight = 32.sp
                ),
                color = OrangePrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Contenedor gris sutil con las opciones
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkSurface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Selecciona tu perfil de acceso",
                        color = TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botón Cliente
                    Button(
                        onClick = { showClienteDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary
                        )
                    ) {
                        Text(
                            text = "Cliente",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    // Botón Operador de Caja
                    Button(
                        onClick = onNavigateToCaja,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangePrimary
                        )
                    ) {
                        Text(
                            text = "Operador de Caja",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }

                    // Botón Administrador
                    OutlinedButton(
                        onClick = onNavigateToAdmin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = OrangePrimary
                        )
                    ) {
                        Text(
                            text = "Administrador",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }
            }
        }
        // VENTANA POP-UP PARA INGRESO DE DATOS DEL CLIENTE
        if (showClienteDialog) {
            AlertDialog(
                onDismissRequest = { showClienteDialog = false },
                containerColor = DarkSurface,
                title = {
                    Text(
                        text = "Datos del Cliente",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Ingresa tu información para registrar tu comanda o pedido",
                            color = TextMuted,
                            fontSize = 14.sp
                        )

                        // Campo Nombre Completo
                        OutlinedTextField(
                            value = nombreCliente,
                            onValueChange = { nombreCliente = it },
                            label = { Text("Nombre completo") },
                            placeholder = {Text("Juan Perez", color = TextMuted)},
                            singleLine = true,
                            textStyle = TextStyle(
                                color = OrangePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangePrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = OrangePrimary,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // Campo Teléfono (Solo permite dígitos)
                        OutlinedTextField(
                            value = telefonoCliente,
                            onValueChange = { entrada ->
                                // Filtrar para que solo acepte números
                                if (entrada.all { it.isDigit() } && entrada.length <= 8) {
                                    telefonoCliente = entrada
                                }
                            },
                            label = { Text("Teléfono") },
                            prefix = {
                                Text(
                                    text = "+569 ",
                                    color=OrangePrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            },
                            placeholder = {Text("12345678", color = TextMuted)},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            textStyle = TextStyle(
                                color = OrangePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangePrimary,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = OrangePrimary,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // Mensaje de Error si la validación falla
                        errorMensaje?.let { mensaje ->
                            Text(
                                text = mensaje,
                                color = ErrorRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            // LÓGICA DE VALIDACIÓN
                            val nombreLimpio = nombreCliente.trim()
                            val telefonoLimpio = telefonoCliente.trim()

                            when {
                                nombreLimpio.length < 10 -> {
                                    errorMensaje = "Por favor, ingresa un nombre completo válido."
                                }
                                telefonoLimpio.length < 8 || telefonoLimpio.length > 9 -> {
                                    errorMensaje = "El numero de teléfono debe ser valido."
                                }
                                else -> {
                                    errorMensaje = null
                                    showClienteDialog = false
                                    // Navega a la siguiente pantalla enviando los datos
                                    onNavigateToCliente(nombreLimpio, telefonoLimpio)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("Ingresar", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClienteDialog = false }) {
                        Text("Cancelar", color = TextMuted)
                    }
                }
            )
        }
    }
}