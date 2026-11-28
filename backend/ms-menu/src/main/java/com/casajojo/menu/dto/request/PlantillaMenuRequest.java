
package com.casajojo.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaMenuRequest {

    // Valida el nombre de la plantilla reutilizable.
    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombrePlantilla;

    // Las nuevas plantillas estarán activas por defecto.
    @NotNull(message = "Debe indicar el estado de la plantilla")
    private Boolean activo = true;

}
