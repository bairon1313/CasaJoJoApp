
package com.casajojo.menu.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoResponse {

    private Integer idPlato;

    private String nombrePlato;

    private String descripcion;

    private Integer precioPlato;

    private boolean disponible;

    // Información de la categoría asociada al plato.
    private Integer idCategoria;

    private String codigoCategoria;

    private String nombreCategoria;

}
