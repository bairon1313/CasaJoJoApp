package com.casajojo.menu.dto.response;

import java.time.LocalDate;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDiarioResponse {

    private Integer idMenu;

    private LocalDate fechaMenu;

    // Estado actual: BORRADOR, PUBLICADO o CERRADO.
    private String estado;

}
