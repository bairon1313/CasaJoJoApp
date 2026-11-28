
package com.casajojo.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    // Valida que el código sea obligatorio y respete el tamaño de la columna.
    @NotBlank(message = "El código de la categoría es obligatorio")
    @Size(max = 30, message = "El código no puede superar los 30 caracteres")
    private String codigoCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombreCategoria;

    // Por defecto, las nuevas categorías estarán activas.
    @NotNull(message = "Debe indicar el estado de la categoría")
    private Boolean activo = true;

}
