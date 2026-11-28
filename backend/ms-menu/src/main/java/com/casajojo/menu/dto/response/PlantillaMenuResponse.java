
package com.casajojo.menu.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaMenuResponse {

    private Integer idPlantilla;

    private String nombrePlantilla;

    // Indica si la plantilla está habilitada para utilizarse.
    private boolean activo;

}
