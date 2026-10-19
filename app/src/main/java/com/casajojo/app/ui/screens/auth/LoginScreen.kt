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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
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

// [Líneas 53-56] Enum para controlar qué perfil está activo en la pantalla principal
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
    // [Líneas 65-68] Estados de UI para el panel lateral derecho y el perfil seleccionado
    var showMenuLateral by remember { mutableStateOf(false) }
    var perfilSeleccionado by remember { mutableStateOf(PerfilAcceso.CLIENTE) }
    val estadoCliente by clienteViewModel.estado.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // [Líneas 75-264] 1. CONTENIDO PRINCIPAL DE LA PANTALLA (TopBar + Branding + Tarjeta + Footer)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- [Líneas 85-128] BARRA SUPERIOR (TOP BAR) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // [Líneas 91-101] Botón Izquierdo: Configuración (En pausa por el momento)
                IconButton(
                    onClick = { /* En pausa: sin acción asignada por ahora */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        tint = TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // [Líneas 103-110] Texto Central Sutil
                Text(
                    text = "LA CASA JO-JO",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp
                )

                // [Líneas 112-127] Botón Derecho: Menú Hamburguesa (3 rayitas) con borde circular
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

            Spacer(modifier = Modifier.height(20.dp))

            // --- [Líneas 132-149] LOGO CIRCULAR CON ANILLO SUTIL ---
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

            // --- [Líneas 153-179] TÍTULOS Y SUBTÍTULOS DE BIENVENIDA ---
            Text(
                text = "TU CASA. TU MOMENTO.",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bienvenido a\nLa Casa Jo-Jo",
                style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 32.sp),
                color = YellowLogo,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Un buen momento empieza aquí.",
                color = TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- [Líneas 183-229] CONTENEDOR CENTRAL DINÁMICO (Animación IE 2.2.2) ---
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
                            titulo = "Ingreso Caja",
                            subtitulo = "Recepción y gestión de comandas",
                            icono = Icons.Default.ShoppingCart,
                            textoBoton = "Entrar a Operador de Caja",
                            onIngresarClick = onNavigateToCaja,
                            onVolverClienteClick = { perfilSeleccionado = PerfilAcceso.CLIENTE }
                        )
                    }

                    PerfilAcceso.ADMIN -> {
                        TarjetaIngresoSecundario(
                            titulo = "Ingreso Admin",
                            subtitulo = "Administración de minuta diaria y precios",
                            icono = Icons.Default.Lock,
                            textoBoton = "Entrar a Panel Administrador",
                            onIngresarClick = onNavigateToAdmin,
                            onVolverClienteClick = { perfilSeleccionado = PerfilAcceso.CLIENTE }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- [Líneas 233-263] PIE DE PÁGINA (FOOTER DE SEGURIDAD Y BRANDING) ---
            Text(
                text = "Hecho para compartir buenos momentos.",
                color = TextMuted.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "LA CASA JO-JO  •  2026",
                color = TextMuted.copy(alpha = 0.5f),
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = TextMuted.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Tus datos están seguros con nosotros",
                    color = TextMuted.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }

        // [Líneas 266-283] 2. FONDO OSCURECIDO (SCRIM) CUANDO SE ABRE EL MENÚ LATERAL
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

        // [Líneas 285-444] 3. MENÚ LATERAL DERECHO A MEDIA PANTALLA (SLIDE-OUT DRAWER - 72% ANCHO)
        AnimatedVisibility(
            visible = showMenuLateral,
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.72f),
                color = DarkSurface,
                shape = RoundedCornerShape(topStart = 28.dp, bottomStart = 28.dp),
                tonalElevation = 12.dp,
                border = BorderStroke(1.dp, TextMuted.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(horizontal = 22.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        // [Líneas 310-349] Encabezado del Drawer: JO-JO LA CASA + Botón Cerrar (✕)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = "JO-JO",
                                    color = YellowLogo,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "LA CASA",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    letterSpacing = 3.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(YellowLogo)
                                )
                            }

                            IconButton(
                                onClick = { showMenuLateral = false },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Cerrar menú",
                                    tint = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "PERFILES DE ACCESO",
                            color = TextMuted,
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Perfil.",
                            color = TextWhite,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 32.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Selecciona tu perfil\npara continuar.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // [Líneas 382-392] Tarjeta 1: Ingreso Cliente
                        TarjetaOpcionPerfilDrawer(
                            titulo = "Ingreso Cliente",
                            subtitulo = "Bienvenido a tu casa",
                            icono = Icons.Default.Person,
                            seleccionado = perfilSeleccionado == PerfilAcceso.CLIENTE,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.CLIENTE
                                showMenuLateral = false // Cierra el panel lateral automáticamente
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // [Líneas 396-406] Tarjeta 2: Ingreso Caja
                        TarjetaOpcionPerfilDrawer(
                            titulo = "Ingreso Caja",
                            subtitulo = "Operador de caja",
                            icono = Icons.Default.ShoppingCart,
                            seleccionado = perfilSeleccionado == PerfilAcceso.CAJA,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.CAJA
                                showMenuLateral = false // Cierra el panel lateral automáticamente
                            }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // [Líneas 410-420] Tarjeta 3: Ingreso Admin
                        TarjetaOpcionPerfilDrawer(
                            titulo = "Ingreso Admin",
                            subtitulo = "Administrador",
                            icono = Icons.Default.Lock,
                            seleccionado = perfilSeleccionado == PerfilAcceso.ADMIN,
                            onClick = {
                                perfilSeleccionado = PerfilAcceso.ADMIN
                                showMenuLateral = false // Cierra el panel lateral automáticamente
                            }
                        )
                    }

                    // [Líneas 423-441] Pie del Drawer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = TextMuted.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Un acceso para cada perfil.\nTus datos siempre seguros.",
                            color = TextMuted.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

// [Líneas 448-698] TARJETA PRINCIPAL POR DEFECTO: INGRESO CLIENTE
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
            // [Líneas 470-503] Encabezado "Ingreso Cliente" con ícono en recuadro oscuro-naranja
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(YellowLogo.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = YellowLogo,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Ingreso Cliente",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Nos alegra tenerte de vuelta",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // [Líneas 507-549] Campo 1: Nombre (y Apellido)
            Text(
                text = "Nombre y Apellido",
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = estadoCliente.nombre,
                onValueChange = onNombreChange,
                placeholder = { Text("Ej. Juan Pérez", color = TextMuted, fontSize = 14.sp) },
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

            // [Líneas 553-608] Campo 2: Número Celular
            Text(
                text = "Número Celular",
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = estadoCliente.telefono,
                onValueChange = { entrada ->
                    if (entrada.all { it.isDigit() } && entrada.length <= 8) {
                        onTelefonoChange(entrada)
                    }
                },
                placeholder = { Text("Ej. 1234 5678", color = TextMuted, fontSize = 14.sp) },
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

            // [Líneas 610-669] Opción Convenio / Perfil Empresa desplegable (RF19)
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
                        placeholder = { Text("Ej. Constructora Pacífico", color = TextMuted, fontSize = 14.sp) },
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

            // [Líneas 673-695] Botón Naranja "Ingresar  →"
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

// [Líneas 700-798] TARJETA PARA ROLES SECUNDARIOS (CAJA Y ADMIN) CON BOTÓN DE RETORNO A CLIENTE
@Composable
private fun TarjetaIngresoSecundario(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    textoBoton: String,
    onIngresarClick: () -> Unit,
    onVolverClienteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, YellowLogo.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(YellowLogo.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = YellowLogo,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = titulo,
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitulo,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                    fontSize = 15.sp,
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

            Spacer(modifier = Modifier.height(8.dp))

            // [Líneas 779-795] Botón rápido para regresar a Ingreso Cliente sin abrir el menú
            TextButton(
                onClick = onVolverClienteClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
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

// [Líneas 800-859] TARJETA INDIVIDUAL DEL MENÚ LATERAL DERECHO (DRAWER)
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