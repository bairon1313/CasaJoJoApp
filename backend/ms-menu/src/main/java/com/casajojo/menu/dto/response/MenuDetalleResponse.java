
package com.casajojo.menu.dto.response;

import java.time.LocalDate;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDetalleResponse {

    private Integer idMenuDetalle;

    // Información del menú diario asociado.
    private Integer idMenu;

    private LocalDate fechaMenu;

    // Información del plato y su categoría.
    private Integer idPlato;

    private String nombrePlato;

    private String descripcion;

    private Integer idCategoria;

    private String codigoCategoria;

    private String nombreCategoria;

    // Precio y disponibilidad específicos del menú diario.
    private Integer precioMenu;

    private boolean disponible;

}
