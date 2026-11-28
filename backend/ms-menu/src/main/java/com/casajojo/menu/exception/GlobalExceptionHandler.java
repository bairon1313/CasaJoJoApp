package com.casajojo.menu.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Controla los recursos que no existen en la base de datos.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.NOT_FOUND,
                ex.getMessage(), request, null);
    }

    // Controla registros duplicados y conflictos.
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
            ResourceConflictException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.CONFLICT,
                ex.getMessage(), request, null);
    }

    // Controla el incumplimiento de las reglas de negocio.
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(
            BusinessException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST,
                ex.getMessage(), request, null);
    }

    // Recupera los errores de validación definidos en los DTO.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return buildResponse(HttpStatus.BAD_REQUEST,
                "Error de validación", request, errors);
    }

    // Controla solicitudes JSON incorrectas o con formatos inválidos.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud contiene datos inválidos",
                request, null);
    }

    // Controla parámetros con tipos de datos incorrectos.
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return buildResponse(HttpStatus.BAD_REQUEST,
                "El parámetro " + ex.getName() + " contiene un valor inválido",
                request, null);
    }

    // Controla restricciones de integridad de la base de datos.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        return buildResponse(HttpStatus.CONFLICT,
                "La operación incumple una restricción de la base de datos",
                request, null);
    }

    // Centraliza la construcción de las respuestas de error.
    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String message,
            HttpServletRequest request, Map<String, String> errors) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                errors
        );

        return ResponseEntity.status(status).body(response);
    }

}
