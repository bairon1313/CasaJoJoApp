package com.casajojo.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.casajojo.app.model.Plato

object PlatoMock {
    val listaPlatos = listOf(
        Plato(
            id = 1,
            nombre = "Empanada de Pino",
            descripcion = "Tradicional empanada chilena al horno con carne picada, cebolla, huevo y aceituna.",
            precio = 2500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 2,
            nombre = "Pastel de Choclo",
            descripcion = "Pastel artesanal con pino de res, pollo, huevo duro y pino dulce de maíz.",
            precio = 6500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 3,
            nombre = "Cazuela de Ave",
            descripcion = "Sopa casera con presa de pollo, papa, zapallo, choclo y verduritas.",
            precio = 5500,
            categoria = "FONDO",
            disponible = true
        ),
        Plato(
            id = 4,
            nombre = "Mote con Huesillo",
            descripcion = "Bebida refrescante chilena con mote de trigo hervido y huesillos deshidratados.",
            precio = 2000,
            categoria = "POSTRE",
            disponible = true
        )
    )
}

@Composable
fun PlatoItem(
    plato: Plato,
    onAgregarClick: (Plato) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plato.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${plato.precio}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = plato.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onAgregarClick(plato) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Agregar al Pedido")
            }
        }
    }
}