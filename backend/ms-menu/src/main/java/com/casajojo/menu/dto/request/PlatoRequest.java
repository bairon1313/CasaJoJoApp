package com.casajojo.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoRequest {

    // Identifica la categoría a la que pertenece el plato.
    @NotNull(message = "La categoría es obligatoria")
    @Positive(message = "El ID de la categoría debe ser mayor a cero")
    private Integer idCategoria;

    @NotBlank(message = "El nombre del plato es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombrePlato;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion = "";

    // Evita registrar platos con precios negativos.
    @NotNull(message = "El precio del plato es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Integer precioPlato;

    @NotNull(message = "Debe indicar la disponibilidad del plato")
    private Boolean disponible = true;

}
