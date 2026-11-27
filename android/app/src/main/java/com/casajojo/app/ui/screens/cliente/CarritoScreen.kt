package com.casajojo.app.ui.screens.cliente

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casajojo.app.data.repository.PedidoRepository
import com.casajojo.app.model.EstadoPedido
import com.casajojo.app.model.Pedido
import com.casajojo.app.model.Plato
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.ClienteMenuViewModel
import com.casajojo.app.viewmodel.ClienteViewModel
import java.util.Calendar

private data class ItemCarritoConEliminar(
    val idSlot: String,
    val plato: Plato,
    val precioCalculado: Int,
    val onEliminar: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    clienteViewModel: ClienteViewModel,
    menuViewModel: ClienteMenuViewModel,
    onVolverClick: () -> Unit
) {
    val context = LocalContext.current
    val estadoCliente by clienteViewModel.estado.collectAsState()
    val estadoMenu by menuViewModel.estado.collectAsState()

    var horaRetiro by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var minutoSeleccionado by remember { mutableStateOf<Int?>(null) }

    var errorHoraMensaje by remember { mutableStateOf<String?>(null) }
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    // Consolidación de items en carrito vinculados a su acción de eliminar
    val listaPedido = remember(estadoMenu) {
        mutableListOf<ItemCarritoConEliminar>().apply {
            estadoMenu.proteina?.let {
                add(ItemCarritoConEliminar("proteina", it, it.precio) { menuViewModel.removerProteina() })
            }
            estadoMenu.platoFondo1?.let {
                add(ItemCarritoConEliminar("fondo1", it, it.precio) { menuViewModel.removerPlatoFondo1() })
            }
            estadoMenu.platoFondo2?.let {
                add(ItemCarritoConEliminar("fondo2", it, it.precio) { menuViewModel.removerPlatoFondo2() })
            }
            estadoMenu.acompanamiento1?.let {
                add(ItemCarritoConEliminar("acomp1", it, it.precio) { menuViewModel.removerAcompanamiento1() })
            }
            estadoMenu.acompanamiento2?.let {
                add(ItemCarritoConEliminar("acomp2", it, it.precio) { menuViewModel.removerAcompanamiento2() })
            }
            estadoMenu.ensalada?.let {
                val precio = if (estadoMenu.porcionDobleEnsalada) it.precio * 2 else it.precio
                add(ItemCarritoConEliminar("ensalada", it, precio) { menuViewModel.removerEnsalada() })
            }
            estadoMenu.postre?.let {
                add(ItemCarritoConEliminar("postre", it, it.precio) { menuViewModel.removerPostre() })
            }
            estadoMenu.bebida?.let {
                add(ItemCarritoConEliminar("bebida", it, it.precio) { menuViewModel.removerBebida() })
            }
        }
    }

    // Método seguro para desplegar el selector de hora al hacer clic
    fun mostrarTimePicker() {
        val calendar = Calendar.getInstance()
        val hActual = horaSeleccionada ?: calendar.get(Calendar.HOUR_OF_DAY)
        val mActual = minutoSeleccionado ?: calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                horaSeleccionada = hourOfDay
                minutoSeleccionado = minute
                horaRetiro = String.format("%02d:%02d", hourOfDay, minute)
                errorHoraMensaje = null
            },
            hActual,
            mActual,
            true
        ).show()
    }

    fun validarYConfirmarPedido() {
        val h = horaSeleccionada
        val m = minutoSeleccionado

        if (h == null || m == null || horaRetiro.isBlank()) {
            errorHoraMensaje = "Debes seleccionar una hora de retiro obligatoriamente."
            return
        }

        val minutosTotales = h * 60 + m
        val inicioPermitido = 9 * 60   // 09:00 AM
        val finPermitido = 15 * 60    // 03:00 PM (15:00)

        if (minutosTotales !in inicioPermitido..finPermitido) {
            errorHoraMensaje = "La hora de retiro debe estar entre las 9:00 AM y las 3:00 PM (15:00 hrs)."
            return
        }

        if (listaPedido.isEmpty()) {
            errorHoraMensaje = "El carrito está vacío. Agrega productos para realizar el pedido."
            return
        }

        // 1. Recopilar platos seleccionados
        val itemsSeleccionados = listOfNotNull(
            estadoMenu.proteina,
            estadoMenu.platoFondo1,
            estadoMenu.platoFondo2,
            estadoMenu.acompanamiento1,
            estadoMenu.acompanamiento2,
            estadoMenu.ensalada,
            estadoMenu.postre,
            estadoMenu.bebida
        )

        // 2. Crear comanda
        val nuevoPedido = Pedido(
            nombreCliente = estadoCliente.nombre,
            telefonoCliente = estadoCliente.telefono,
            platosSeleccionados = itemsSeleccionados,
            observaciones = estadoMenu.observacionesAlimentarias,
            horaRetiro = horaRetiro,
            estado = EstadoPedido.RECIBIDO
        )

        // 3. Enviar al repositorio compartido
        PedidoRepository.agregarPedido(nuevoPedido)

        errorHoraMensaje = null
        mostrarDialogoExito = true
    }

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
            // SECCIÓN 1: LISTA DEL CARRITO CON BOTÓN DE ELIMINAR REDONDO
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
                            items(
                                items = listaPedido,
                                key = { it.idSlot }
                            ) { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.plato.nombre,
                                            color = TextWhite,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = item.plato.categoria,
                                            color = YellowLogo,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Text(
                                        text = "$${item.precioCalculado}",
                                        color = YellowLogo,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )

                                    // Botón de eliminar redondo
                                    Box(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clip(CircleShape)
                                            .background(ErrorRed.copy(alpha = 0.2f))
                                            .clickable { item.onEliminar() },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Eliminar plato",
                                            tint = ErrorRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 10.dp),
                                    color = TextMuted.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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

            // SECCIÓN 2: DATOS DEL CLIENTE Y HORA DE RETIRO
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

                    // Campo interactivo para selección de hora
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = if (horaRetiro.isBlank()) "" else "$horaRetiro hrs",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Hora obligatoria de retiro (9:00 AM - 3:00 PM)", color = TextMuted) },
                            placeholder = { Text("Selecciona la hora", color = TextMuted) },
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
                                unfocusedBorderColor = if (errorHoraMensaje != null) ErrorRed else TextMuted.copy(alpha = 0.3f),
                                focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                                unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f)
                            )
                        )

                        // Superposición transparente para abrir el TimePicker
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { mostrarTimePicker() }
                        )
                    }

                    // Mensaje de error de hora si no es válida
                    errorHoraMensaje?.let { msg ->
                        Text(
                            text = msg,
                            color = ErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // BOTÓN REALIZAR PEDIDO
            Button(
                onClick = { validarYConfirmarPedido() },
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

    // DIÁLOGO DE CONFIRMACIÓN DE ÉXITO
    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = DarkSurface,
            title = {
                Text(
                    text = "¡Pedido Confirmado!",
                    color = YellowLogo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "Pedido realizado con éxito. Gracias por comprar en La Casa de Jojo.",
                    color = TextWhite,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoExito = false
                        menuViewModel.vaciarCarrito()
                        onVolverClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowLogo,
                        contentColor = DarkBackground
                    )
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}