package com.casajojo.app.ui.screens.cliente

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casajojo.app.R
import com.casajojo.app.model.OpcionMenu
import com.casajojo.app.model.Plato
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.ClienteMenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nombreCliente: String,
    telefonoCliente: String,
    onVerCarritoClick: () -> Unit,
    cantidadProductosCarrito: Int = 0,
    menuViewModel: ClienteMenuViewModel = viewModel()
) {
    val estado by menuViewModel.estado.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Menú del Día",
                        color = YellowLogo,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                IconButton(
                    onClick = onVerCarritoClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(DarkSurface)
                ) {
                    BadgedBox(
                        badge = {
                            if (cantidadProductosCarrito > 0) {
                                Badge(
                                    containerColor = OrangePrimary,
                                    contentColor = TextWhite
                                ) {
                                    Text(
                                        text = cantidadProductosCarrito.toString(),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver Carrito",
                            tint = YellowLogo,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total a pagar", color = TextMuted, fontSize = 12.sp)
                        Text(
                            text = "$${estado.precioTotal}",
                            color = YellowLogo,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Button(
                        onClick = onVerCarritoClick,
                        colors = ButtonDefaults.buttonColors(containerColor = YellowLogo)
                    ) {
                        Text("Ver Carrito", color = TextWhite, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Bienvenido $nombreCliente",
                            color = YellowLogo,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Selecciona tus platillos para armar tu comanda.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Modalidades de Menú
            item {
                Text(
                    text = "Modalidad del Menú",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        OpcionMenu.ESTANDAR,
                        OpcionMenu.DOBLE_PROTEINA,
                        OpcionMenu.DOBLE_ACOMPANAMIENTO,
                        OpcionMenu.SOLO_ENSALADA
                    ).forEach { opcion ->
                        FilterChip(
                            selected = estado.tipoCombinacion == opcion,
                            onClick = { menuViewModel.onTipoCombinacionChange(opcion) },
                            label = { Text(opcion.etiqueta, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = YellowLogo,
                                selectedLabelColor = TextWhite,
                                containerColor = DarkSurface,
                                labelColor = TextMuted
                            )
                        )
                    }
                }
            }

            // =========================================================================
            // SECCIÓN: PROTEÍNAS Y PLATOS DE FONDO (INDEPENDIENTES)
            // =========================================================================
            if (estado.tipoCombinacion != OpcionMenu.SOLO_ENSALADA) {

                // --- 1. SECCIÓN DE PROTEÍNAS  ---
                if (estado.proteinas.isNotEmpty()) {
                    item {
                        Text(
                            text = "Proteínas",
                            color = YellowLogo,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    items(estado.proteinas) { plato ->
                        TarjetaComidaJoJo(
                            plato = plato,
                            seleccionado = estado.proteina?.id == plato.id, // VINCULADO A PROTEINA
                            onClick = { menuViewModel.onProteinaSelect(plato) } // LLAMA A ONPROTEINASELECT
                        )
                    }
                }

                // --- 2. SECCIÓN DE PLATOS DE FONDO  ---
                if (estado.fondos.isNotEmpty()) {
                    item {
                        Text(
                            text = if (estado.tipoCombinacion == OpcionMenu.DOBLE_PROTEINA)
                                "Plato de Fondo 1"
                            else
                                "Plato de Fondo",
                            color = YellowLogo,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(estado.fondos) { plato ->
                        TarjetaComidaJoJo(
                            plato = plato,
                            seleccionado = estado.platoFondo1?.id == plato.id, // VINCULADO A PLATOFONDO1
                            onClick = { menuViewModel.onPlatoFondo1Select(plato) } // LLAMA A ONPLATOFONDO1SELECT
                        )
                    }
                }

                // --- 3. SEGUNDO PLATO DE FONDO  ---
                if (estado.tipoCombinacion == OpcionMenu.DOBLE_PROTEINA && estado.fondos.isNotEmpty()) {
                    item {
                        Text(
                            text = "Plato de Fondo 2",
                            color = YellowLogo,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(estado.fondos) { plato ->
                        TarjetaComidaJoJo(
                            plato = plato,
                            seleccionado = estado.platoFondo2?.id == plato.id,
                            onClick = { menuViewModel.onPlatoFondo2Select(plato) }
                        )
                    }
                }
            }

            // Acompañamientos
            if (estado.tipoCombinacion != OpcionMenu.SOLO_ENSALADA && estado.tipoCombinacion != OpcionMenu.DOBLE_PROTEINA) {
                item {
                    Text(
                        if (estado.tipoCombinacion == OpcionMenu.DOBLE_ACOMPANAMIENTO) "Acompañamiento 1" else "Acompañamiento",
                        color = YellowLogo,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(estado.acompanamientos) { plato ->
                    TarjetaComidaJoJo(
                        plato = plato,
                        seleccionado = estado.acompanamiento1?.id == plato.id,
                        onClick = { menuViewModel.onAcompanamiento1Select(plato) }
                    )
                }

                if (estado.tipoCombinacion == OpcionMenu.DOBLE_ACOMPANAMIENTO) {
                    item {
                        Text(
                            "Acompañamiento 2",
                            color = YellowLogo,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(estado.acompanamientos) { plato ->
                        TarjetaComidaJoJo(
                            plato = plato,
                            seleccionado = estado.acompanamiento2?.id == plato.id,
                            onClick = { menuViewModel.onAcompanamiento2Select(plato) }
                        )
                    }
                }
            }

            // Ensaladas
            item {
                Text(
                    "Ensalada",
                    color = YellowLogo,
                    fontWeight = FontWeight.Bold
                )
            }
            items(estado.ensaladas) { plato ->
                TarjetaComidaJoJo(
                    plato = plato,
                    seleccionado = estado.ensalada?.id == plato.id,
                    onClick = { menuViewModel.onEnsaladaSelect(plato) }
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = estado.porcionDobleEnsalada,
                            onCheckedChange = { menuViewModel.onPorcionDobleEnsaladaChange(it) },
                            colors = CheckboxDefaults.colors(checkedColor = YellowLogo)
                        )
                        Text("Doble porción de ensalada", color = TextWhite, fontSize = 13.sp)
                    }
                }
            }

            // Postres
            if (estado.postres.isNotEmpty()) {
                item {
                    Text(
                        "Postre",
                        color = YellowLogo,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(estado.postres) { plato ->
                    TarjetaComidaJoJo(
                        plato = plato,
                        seleccionado = estado.postre?.id == plato.id,
                        onClick = { menuViewModel.onPostreSelect(plato) }
                    )
                }
            }

            // Bebidas
            if (estado.bebidas.isNotEmpty()) {
                item {
                    Text(
                        "Bebida",
                        color = YellowLogo,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(estado.bebidas) { plato ->
                    TarjetaComidaJoJo(
                        plato = plato,
                        seleccionado = estado.bebida?.id == plato.id,
                        onClick = { menuViewModel.onBebidaSelect(plato) }
                    )
                }
            }

            // Observaciones
            item {
                Text(
                    text = "Observaciones alimentarias",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = estado.observacionesAlimentarias,
                    onValueChange = { menuViewModel.onObservacionesChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Sin sal, aderezo aparte, alergia",
                            color = TextMuted,
                            fontSize = 13.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = YellowLogo,
                        unfocusedBorderColor = DarkSurface,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun TarjetaComidaJoJo(
    plato: Plato,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) DarkSurface.copy(alpha = 0.95f) else DarkSurface
        ),
        border = if (seleccionado) BorderStroke(1.5.dp, OrangePrimary) else null,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plato.nombre,
                    color = if (seleccionado) YellowLogo else TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                if (plato.descripcion.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = plato.descripcion,
                        color = TextMuted,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "+$${plato.precio}",
                color = YellowLogo,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (seleccionado) YellowLogo else DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (seleccionado) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (seleccionado) "Seleccionado" else "Agregar",
                    tint = if (seleccionado) TextWhite else TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}