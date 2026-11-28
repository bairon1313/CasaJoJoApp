
package com.casajojo.menu.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// Representa los datos de una categoría que devuelve la API.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private Integer idCategoria;

    private String codigoCategoria;

    private String nombreCategoria;

    private boolean activo;

}
