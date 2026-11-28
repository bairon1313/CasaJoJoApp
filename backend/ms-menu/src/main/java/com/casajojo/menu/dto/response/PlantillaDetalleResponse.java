
package com.casajojo.menu.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaDetalleResponse {

    private Integer idPlantillaDetalle;

    // Información de la plantilla asociada.
    private Integer idPlantilla;

    private String nombrePlantilla;

    // Información del plato incorporado a la plantilla.
    private Integer idPlato;

    private String nombrePlato;

    private String descripcion;

    private Integer precioPlato;

    private boolean disponible;

    // Información de la categoría del plato.
    private Integer idCategoria;

    private String codigoCategoria;

    private String nombreCategoria;

}
