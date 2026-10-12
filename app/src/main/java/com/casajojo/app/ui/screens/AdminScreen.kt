package com.casajojo.app.ui.screens

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
import com.casajojo.app.R
import com.casajojo.app.model.ComponenteMenu
import com.casajojo.app.ui.theme.*
import com.casajojo.app.ui.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: MenuViewModel
) {
    var nombreComponente by remember { mutableStateOf("") }
    var precioComponente by remember { mutableStateOf("") }

    val categorias = listOf("PROTEINA", "ACOMPAÑAMIENTO", "ENSALADA", "POSTRE", "BEBIDA", "EXTRA")
    var categoriaSeleccionada by remember { mutableStateOf(categorias.first()) }

    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var nuevoPrecioInput by remember { mutableStateOf(viewModel.precioMenuDelDia.toString()) }

    // EJEMPLOS DINÁMICOS SEGÚN CATEGORÍA
    val ejemploPlaceholder = when (categoriaSeleccionada) {
        "PROTEINA" -> "Pollo al horno, Carne mechada, Chuleta"
        "ACOMPAÑAMIENTO" -> "Puré de papas, Arroz primaveral, Papas fritas"
        "ENSALADA" -> "Ensalada chilena, Tomate mayo, Mix verde"
        "POSTRE" -> "Leche asada, Flan de vainilla, Jalea"
        "BEBIDA" -> "Coca-Cola 350ml, Jugo natural de piña"
        "EXTRA" -> "Porción de mayo casera, Huevo frito"
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
                        // 1. IZQUIERDA: LOGO DE LA CASA DE JOJO
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
                                    text = "Permite combinar 1 Proteína +\nAcompañamiento + Ensalada",
                                    color = TextMuted,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                            Switch(
                                checked = viewModel.menuDelDiaActivo,
                                onCheckedChange = { viewModel.toggleMenuDelDiaActivo() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextWhite,
                                    checkedTrackColor = YellowLogo,
                                    uncheckedThumbColor = TextMuted,
                                    uncheckedTrackColor = DarkBackground
                                )
                            )
                        }

                        if (viewModel.menuDelDiaActivo) {
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
                                    onClick = {
                                        val precioInt = nuevoPrecioInput.toIntOrNull()
                                        if (precioInt != null && precioInt > 0) {
                                            viewModel.actualizarPrecioMenu(precioInt)
                                        }
                                    },
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
                                    viewModel.agregarComponente(
                                        nombre = nombreComponente,
                                        categoria = categoriaSeleccionada,
                                        precioIndividual = precioInt
                                    )
                                    nombreComponente = ""
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
                    text = "Lista de Opciones (${viewModel.listaComponentes.size})",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // SECCIÓN 4: ELEMENTOS REGISTRADOS
            items(
                items = viewModel.listaComponentes,
                key = { it.id }
            ) { componente ->
                ItemComponenteAdmin(
                    componente = componente,
                    onToggle = { viewModel.toggleDisponible(componente) },
                    onEliminar = { viewModel.eliminarComponente(componente) }
                )
            }
        }
    }
}

@Composable
private fun ItemComponenteAdmin(
    componente: ComponenteMenu,
    onToggle: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (componente.disponible) DarkSurface else DarkSurface.copy(alpha = 0.5f)
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
                        text = componente.categoria,
                        color = YellowLogo,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = componente.nombre,
                    color = if (componente.disponible) TextWhite else TextMuted,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                if (componente.precioIndividual > 0) {
                    Text(
                        text = "Precio individual: $${componente.precioIndividual}",
                        color = YellowLogo,
                        fontSize = 12.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = componente.disponible,
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