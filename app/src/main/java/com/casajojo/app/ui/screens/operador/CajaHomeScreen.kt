package com.casajojo.app.ui.screens.caja

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.casajojo.app.model.EstadoPedido
import com.casajojo.app.model.Pedido
import com.casajojo.app.ui.theme.*
import com.casajojo.app.viewmodel.CajaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CajaScreen(
    cajaViewModel: CajaViewModel = viewModel(),
    onVolverClick: () -> Unit = {}
) {
    val listaPedidos by cajaViewModel.pedidos.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comandas de Caja",
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
                .padding(16.dp)
        ) {
            if (listaPedidos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay pedidos ingresados por el momento.",
                        color = TextMuted,
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(
                        items = listaPedidos,
                        key = { it.id }
                    ) { pedido ->
                        TarjetaPedidoCaja(
                            pedido = pedido,
                            onMarcarListo = { cajaViewModel.marcarComoListo(pedido.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPedidoCaja(
    pedido: Pedido,
    onMarcarListo: () -> Unit
) {
    val esListo = pedido.estado == EstadoPedido.LISTO || pedido.estado == EstadoPedido.ENTREGADO
    val totalCalculado = pedido.platosSeleccionados.sumOf { it.precio }

    // Color del badge según el estado del enum
    val (colorFondoBadge, colorTextoBadge) = when (pedido.estado) {
        EstadoPedido.RECIBIDO -> OrangePrimary.copy(alpha = 0.2f) to OrangePrimary
        EstadoPedido.EN_PREPARACION -> YellowLogo.copy(alpha = 0.2f) to YellowLogo
        EstadoPedido.LISTO -> Color(0xFF2E7D32) to Color.White
        EstadoPedido.ENTREGADO -> DarkBackground to TextMuted
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        border = if (esListo) null else BorderStroke(1.dp, YellowLogo)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Encabezado: Hora de Retiro e Insignia de Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Hora retiro",
                        tint = YellowLogo,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Retiro: ${pedido.horaRetiro} hrs",
                        color = YellowLogo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = colorFondoBadge,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = pedido.estado.etiqueta.uppercase(),
                        color = colorTextoBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Datos del Cliente
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pedido.nombreCliente,
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = pedido.telefonoCliente,
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = TextMuted.copy(alpha = 0.2f)
            )

            // Detalle de Platos Seleccionados
            Text(
                text = "Detalle del Pedido:",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            pedido.platosSeleccionados.forEach { plato ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "• ${plato.nombre}", color = TextWhite, fontSize = 14.sp)
                    Text(text = "$${plato.precio}", color = TextMuted, fontSize = 14.sp)
                }
            }

            if (pedido.observaciones.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Obs: ${pedido.observaciones}",
                    color = OrangePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = TextMuted.copy(alpha = 0.2f)
            )

            // Total a Pagar y Botón para cambiar estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = "$$totalCalculado",
                        color = YellowLogo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Button(
                    onClick = onMarcarListo,
                    enabled = !esListo,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = YellowLogo,
                        disabledContainerColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (esListo) TextMuted else DarkBackground,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (esListo) pedido.estado.etiqueta else "Marcar Listo",
                        color = if (esListo) TextMuted else DarkBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}