package com.casajojo.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetalleRequest {

    // Identifica el menú diario al que se incorporará el plato.
    @NotNull(message = "El menú diario es obligatorio")
    @Positive(message = "El ID del menú debe ser mayor a cero")
    private Integer idMenu;

    // Identifica el plato existente en el catálogo.
    @NotNull(message = "El plato es obligatorio")
    @Positive(message = "El ID del plato debe ser mayor a cero")
    private Integer idPlato;

    // Permite establecer un precio específico para el menú diario.
    @NotNull(message = "El precio del menú es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Integer precioMenu;

    @NotNull(message = "Debe indicar la disponibilidad del plato")
    private Boolean disponible = true;

}
