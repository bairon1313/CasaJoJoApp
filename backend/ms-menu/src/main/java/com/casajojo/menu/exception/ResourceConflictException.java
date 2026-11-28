package com.casajojo.menu.exception;

// Excepción utilizada cuando existe un conflicto con un recurso registrado.
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {

        super(message);

    }

}
