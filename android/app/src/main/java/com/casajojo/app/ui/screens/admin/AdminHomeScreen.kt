package com.casajojo.app.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casajojo.app.R
import com.casajojo.app.model.Plato
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel()
) {
    // Observamos la lista reactiva centralizada en MenuRepository
    val listaPlatos by viewModel.listaPlatos.collectAsState()

    var nombreComponente by remember { mutableStateOf("") }
    var descripcionComponente by remember { mutableStateOf("") }
    var precioComponente by remember { mutableStateOf("") }

    // Agregada la categoría PROTEINA a la lista
    val categorias = listOf("PROTEINA", "FONDO", "ACOMPAÑAMIENTO", "ENSALADA", "POSTRE", "BEBIDA")
    var categoriaSeleccionada by remember { mutableStateOf(categorias.first()) }

    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Estado local para la opción de Combo
    var menuDelDiaActivo by remember { mutableStateOf(true) }
    var nuevoPrecioInput by remember { mutableStateOf("4500") }

    val ejemploPlaceholder = when (categoriaSeleccionada) {
        "PROTEINA" -> "Pollo al horno, Carne mechada, Chuleta, Pescado"
        "FONDO" -> "Cazuela, Porotos con riendas, Guiso"
        "ACOMPAÑAMIENTO" -> "Puré de papas, Arroz primaveral, Papas fritas"
        "ENSALADA" -> "Ensalada chilena, Tomate mayo, Mix verde"
        "POSTRE" -> "Leche asada, Flan de vainilla, Jalea"
        "BEBIDA" -> "Coca-Cola 350ml, Jugo natural"
        else -> "Opción del menú"
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo La Casa De JoJo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }

                        Text(
                            text = "Panel Administrador",
                            color = YellowLogo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            // SECCIÓN 1: TARJETA DEL "MENÚ DEL DÍA"
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Menú del Día",
                                    color = TextWhite,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Permite combinar opciones estándar",
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                            Switch(
                                checked = menuDelDiaActivo,
                                onCheckedChange = { menuDelDiaActivo = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextWhite,
                                    checkedTrackColor = YellowLogo,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkBackground
                                )
                            )
                        }

                        if (menuDelDiaActivo) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = nuevoPrecioInput,
                                    onValueChange = { entrada ->
                                        if (entrada.all { it.isDigit() }) nuevoPrecioInput = entrada
                                    },
                                    label = { Text("Precio Combo ($)") },
                                    placeholder = { Text("4500", color = TextMuted) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = YellowLogo,
                                        unfocusedBorderColor = TextMuted,
                                        focusedLabelColor = YellowLogo,
                                        unfocusedLabelColor = TextMuted,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite
                                    )
                                )

                                Button(
                                    onClick = { /* Actualización de precio combo si aplica */ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = YellowLogo,
                                        contentColor = DarkBackground
                                    )
                                ) {
                                    Text("Actualizar", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // SECCIÓN 2: TARJETA "AGREGAR OPCIONES"
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Agregar Opciones Para La Comanda",
                            color = TextWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Categorías en Chips
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(categorias) { cat ->
                                FilterChip(
                                    selected = (cat == categoriaSeleccionada),
                                    onClick = {
                                        categoriaSeleccionada = cat
                                        nombreComponente = ""
                                        descripcionComponente = ""
                                        precioComponente = ""
                                        errorMensaje = null
                                    },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = YellowLogo,
                                        selectedLabelColor = DarkBackground,
                                        containerColor = DarkBackground,
                                        labelColor = TextMuted
                                    )
                                )
                            }
                        }

                        // Campo Nombre
                        OutlinedTextField(
                            value = nombreComponente,
                            onValueChange = { nombreComponente = it },
                            label = { Text("Nombre de la Opción") },
                            placeholder = { Text(ejemploPlaceholder, color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YellowLogo,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = YellowLogo,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // Campo Descripción
                        OutlinedTextField(
                            value = descripcionComponente,
                            onValueChange = { descripcionComponente = it },
                            label = { Text("Descripción (Opcional)") },
                            placeholder = { Text("Ej: Acompañado de arroz graneado", color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YellowLogo,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = YellowLogo,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        // Campo Precio
                        OutlinedTextField(
                            value = precioComponente,
                            onValueChange = { entrada ->
                                if (entrada.all { it.isDigit() }) precioComponente = entrada
                            },
                            label = { Text("Precio Individual Suelto ($)") },
                            placeholder = { Text("2000", color = TextMuted) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = YellowLogo,
                                unfocusedBorderColor = TextMuted,
                                focusedLabelColor = YellowLogo,
                                unfocusedLabelColor = TextMuted,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        errorMensaje?.let {
                            Text(text = it, color = ErrorRed, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                val precioInt = precioComponente.toIntOrNull()
                                if (nombreComponente.isBlank()) {
                                    errorMensaje = "Ingresa el nombre de la opción."
                                } else if (precioInt == null || precioInt <= 0) {
                                    errorMensaje = "Ingresa un precio individual válido."
                                } else {
                                    viewModel.agregarPlato(
                                        nombre = nombreComponente.trim(),
                                        descripcion = descripcionComponente.ifBlank { "Opción de la minuta" }.trim(),
                                        precio = precioInt,
                                        categoria = categoriaSeleccionada
                                    )
                                    nombreComponente = ""
                                    descripcionComponente = ""
                                    precioComponente = ""
                                    errorMensaje = null
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = YellowLogo,
                                contentColor = DarkBackground
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = DarkBackground)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Guardar Opción", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // SECCIÓN 3: TÍTULO DE LISTA
            item {
                Text(
                    text = "Lista de Opciones (${listaPlatos.size})",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // SECCIÓN 4: ELEMENTOS REGISTRADOS
            items(
                items = listaPlatos,
                key = { it.id }
            ) { plato ->
                ItemComponenteAdmin(
                    plato = plato,
                    onToggle = { viewModel.toggleDisponibilidad(plato.id) },
                    onEliminar = { viewModel.eliminarPlato(plato.id) }
                )
            }
        }
    }
}

@Composable
private fun ItemComponenteAdmin(
    plato: Plato,
    onToggle: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (plato.disponible) DarkSurface else DarkSurface.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = YellowLogo.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = plato.categoria,
                        color = YellowLogo,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = plato.nombre,
                    color = if (plato.disponible) TextWhite else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                if (plato.descripcion.isNotBlank()) {
                    Text(
                        text = plato.descripcion,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                if (plato.precio > 0) {
                    Text(
                        text = "Precio individual: $${plato.precio}",
                        color = YellowLogo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = plato.disponible,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = TextWhite,
                        checkedTrackColor = YellowLogo,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = DarkBackground
                    )
                )

                IconButton(onClick = onEliminar) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Borrar",
                        tint = ErrorRed
                    )
                }
            }
        }
    }
}