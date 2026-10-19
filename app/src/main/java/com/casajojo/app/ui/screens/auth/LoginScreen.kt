package com.casajojo.app.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casajojo.app.R
import com.casajojo.app.model.ClienteUiState
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.ClienteViewModel

enum class PerfilAcceso {
    CLIENTE, CAJA, ADMIN
}

@Composable
fun LoginScreen(
    clienteViewModel: ClienteViewModel = viewModel(),
    onNavigateToCliente: (nombre: String, telefono: String) -> Unit,
    onNavigateToCaja: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    var showMenuLateral by remember { mutableStateOf(false) }
    var perfilSeleccionado by remember { mutableStateOf(PerfilAcceso.CLIENTE) }
    val estadoCliente by clienteViewModel.estado.collectAsState()

    // CONTENEDOR RAIZ
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // 1. CONTENIDO PRINCIPAL DE LA PANTALLA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- BARRA SUPERIOR (TOP BAR) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = { showMenuLateral = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(DarkSurface)
                        .border(1.dp, TextMuted.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir menú de perfiles",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- LOGO CIRCULAR ---
            Box(
                modifier = Modifier
                    .size(136.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
                    .border(2.dp, YellowLogo.copy(alpha = 0.25f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo La Casa De JoJo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Bienvenido a\nLa Casa Jo-Jo",
                style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 32.sp),
                color = YellowLogo,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- CONTENEDOR CENTRAL DINÁMICO ---
            AnimatedContent(
                targetState = perfilSeleccionado,
                label = "TransicionPerfilAcceso"
            ) { perfilActual ->
                when (perfilActual) {
                    PerfilAcceso.CLIENTE -> {
                        TarjetaIngresoCliente(
                            estadoCliente = estadoCliente,
                            onNombreChange = { clienteViewModel.onNombreChange(it) },
                            onTelefonoChange = { clienteViewModel.onTelefonoChange(it) },
                            onEsPerfilEmpresaChange = { clienteViewModel.onEsPerfilEmpresaChange(it) },
                            onNombreEmpresaChange = { clienteViewModel.onNombreEmpresaChange(it) },
                            onIngresarClick = {
                                if (clienteViewModel.validarFormulario()) {
                                    onNavigateToCliente(
                                        estadoCliente.nombre.trim(),
                                        estadoCliente.telefono.trim()
                                    )
                                }
                            }
                        )
                    }

                    PerfilAcceso.CAJA -> {
                        TarjetaIngresoSecundario(
                            titulo = "Acceso Operador de Caja",
                            subtitulo = "Acceso directo a la recepción y gestión de comandas.",
                            textoBoton = "Ingresar a Caja",
                            onIngresarClick = onNavigateToCaja,
                            onVolverClienteClick = { perfilSeleccionado = PerfilAcceso.CLIENTE }
                        )
                    }

                    PerfilAcceso.ADMIN -> {
                        TarjetaIngresoSecundario(
                            titulo = "Acceso Administrador",
                            subtitulo = "Acceso directo a la administración de minuta y precios.",
                            textoBoton = "Ingresar a Admin",
                            onIngresarClick = onNavigateToAdmin,
                            onVolverClienteClick = { perfilSeleccionado = PerfilAcceso.CLIENTE }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // 2. FONDO OSCURECIDO (SCRIM)
        AnimatedVisibility(
            visible = showMenuLateral,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.68f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        showMenuLateral = false
                    }
            )
        }

        // 3. MENÚ LATERAL DERECHO (DRAWER)
        AnimatedVisibility(
            visible = showMenuLateral,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.75f),
                color = Color(0xFF181818),
                shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
                tonalElevation = 12.dp,
                border = BorderStroke(1.dp, TextMuted.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // Encabezado con Botón Cerrar (✕)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "LA CASA JO-JO",
                                    color = YellowLogo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Seleccionar Perfil",
                                    color = TextWhite,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { showMenuLateral = false },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar menú",
                                    tint = TextWhite,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Elige el tipo de cuenta para acceder a las funciones del sistema.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Opciones de Perfil
                        TarjetaOpcionPerfilDrawer(
                            titulo = "Cliente",
                            subtitulo = "Armar comanda y hora de retiro",
                            icono = Icons.Default.Person,
                            seleccionado = perfilSeleccionado == PerfilAcceso.CLIENTE,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.CLIENTE
                                showMenuLateral = false
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TarjetaOpcionPerfilDrawer(
                            titulo = "Operador de Caja",
                            subtitulo = "Recepción y cambio de estados",
                            icono = Icons.Default.ShoppingCart,
                            seleccionado = perfilSeleccionado == PerfilAcceso.CAJA,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.CAJA
                                showMenuLateral = false
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TarjetaOpcionPerfilDrawer(
                            titulo = "Administrador",
                            subtitulo = "Gestión de menú y precios",
                            icono = Icons.Default.Lock,
                            seleccionado = perfilSeleccionado == PerfilAcceso.ADMIN,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.ADMIN
                                showMenuLateral = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// TARJETA PRINCIPAL POR DEFECTO: INGRESO CLIENTE
@Composable
private fun TarjetaIngresoCliente(
    estadoCliente: ClienteUiState,
    onNombreChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onEsPerfilEmpresaChange: (Boolean) -> Unit,
    onNombreEmpresaChange: (String) -> Unit,
    onIngresarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, TextMuted.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Ingresa tus datos para la comanda",
                color = TextMuted,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo 1: Nombre Completo
            Text(
                text = "Nombre Completo",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = estadoCliente.nombre,
                onValueChange = onNombreChange,
                placeholder = { Text("Juan Pérez", color = TextMuted, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (estadoCliente.errores.nombre != null) ErrorRed else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                isError = estadoCliente.errores.nombre != null,
                supportingText = {
                    estadoCliente.errores.nombre?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = ErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = TextWhite, fontSize = 15.sp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowLogo,
                    unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                    focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                    unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                    errorBorderColor = ErrorRed
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo 2: Número Celular
            Text(
                text = "Número Celular",
                color = TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = estadoCliente.telefono,
                onValueChange = { entrada ->
                    if (entrada.all { it.isDigit() } && entrada.length <= 8) {
                        onTelefonoChange(entrada)
                    }
                },
                placeholder = { Text("12345678", color = TextMuted, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = if (estadoCliente.errores.telefono != null) ErrorRed else TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                prefix = {
                    Text(
                        text = "+569 ",
                        color = YellowLogo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                },
                isError = estadoCliente.errores.telefono != null,
                supportingText = {
                    estadoCliente.errores.telefono?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = ErrorRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(color = TextWhite, fontSize = 15.sp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = YellowLogo,
                    unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                    focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                    unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                    errorBorderColor = ErrorRed
                )
            )

            // Opción Convenio / Perfil Empresa
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = estadoCliente.esPerfilEmpresa,
                    onCheckedChange = onEsPerfilEmpresaChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = YellowLogo,
                        uncheckedColor = TextMuted
                    )
                )
                Text(
                    text = "Cliente con Convenio / Perfil Empresa",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            AnimatedVisibility(visible = estadoCliente.esPerfilEmpresa) {
                Column(modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)) {
                    Text(
                        text = "Nombre de la Empresa",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = estadoCliente.nombreEmpresa,
                        onValueChange = onNombreEmpresaChange,
                        placeholder = {
                            Text("Constructora Pacífico", color = TextMuted, fontSize = 14.sp)
                        },
                        isError = estadoCliente.errores.nombreEmpresa != null,
                        supportingText = {
                            estadoCliente.errores.nombreEmpresa?.let { mensaje ->
                                Text(
                                    text = mensaje,
                                    color = ErrorRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = TextWhite, fontSize = 15.sp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = YellowLogo,
                            unfocusedBorderColor = TextMuted.copy(alpha = 0.3f),
                            focusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                            unfocusedContainerColor = DarkBackground.copy(alpha = 0.6f),
                            errorBorderColor = ErrorRed
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Botón Naranja
            Button(
                onClick = onIngresarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = YellowLogo)
            ) {
                Text(
                    text = "Ingresar",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// TARJETA PARA ROLES SECUNDARIOS (CAJA Y ADMIN)
@Composable
private fun TarjetaIngresoSecundario(
    titulo: String,
    subtitulo: String,
    textoBoton: String,
    onIngresarClick: () -> Unit,
    onVolverClienteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, TextMuted.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = titulo,
                color = YellowLogo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = subtitulo,
                color = TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onIngresarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = YellowLogo)
            ) {
                Text(
                    text = textoBoton,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
            }

            TextButton(
                onClick = onVolverClienteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Volver a Ingreso Cliente",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
        }
    }
}

// TARJETA INDIVIDUAL DEL MENÚ LATERAL DERECHO (DRAWER)
@Composable
private fun TarjetaOpcionPerfilDrawer(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (seleccionado) YellowLogo.copy(alpha = 0.14f) else DarkBackground.copy(alpha = 0.55f)
        ),
        border = BorderStroke(
            width = if (seleccionado) 1.5.dp else 1.dp,
            color = if (seleccionado) YellowLogo else YellowLogo.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = YellowLogo,
                    modifier = Modifier.size(22.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = YellowLogo,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = titulo,
                color = TextWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitulo,
                color = TextMuted,
                fontSize = 12.sp
            )
        }
    }
}