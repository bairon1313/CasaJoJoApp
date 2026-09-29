package com.casajojo.app.model

enum class EstadoPedido(val etiqueta: String) {
    RECIBIDO("Recibido"),
    EN_PREPARACION("En Preparacion"),
    LISTO("Listo para retiro"),
    ENTREGADO("Entregado")
}