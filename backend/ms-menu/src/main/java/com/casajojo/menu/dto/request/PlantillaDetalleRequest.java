
package com.casajojo.menu.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaDetalleRequest {

    // Identifica la plantilla a la que se incorporará el plato.
    @NotNull(message = "La plantilla es obligatoria")
    @Positive(message = "El ID de la plantilla debe ser mayor a cero")
    private Integer idPlantilla;

    // Identifica el plato existente en el catálogo.
    @NotNull(message = "El plato es obligatorio")
    @Positive(message = "El ID del plato debe ser mayor a cero")
    private Integer idPlato;

}
