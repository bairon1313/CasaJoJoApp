
package com.casajojo.menu.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDiarioRequest {

    // Fecha para la cual se registrará el menú diario.
    @NotNull(message = "La fecha del menú es obligatoria")
    private LocalDate fechaMenu;

}
