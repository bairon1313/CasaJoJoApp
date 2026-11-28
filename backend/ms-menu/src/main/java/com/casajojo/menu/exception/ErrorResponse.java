
package com.casajojo.menu.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    // Fecha y hora en que ocurrió el error.
    private LocalDateTime timestamp;

    // Código HTTP y tipo de error.
    private int status;

    private String error;

    // Mensaje descriptivo del problema.
    private String message;

    // Ruta del endpoint donde ocurrió el error.
    private String path;

    // Almacena los errores de validación de cada campo.
    private Map<String, String> errors;

}
