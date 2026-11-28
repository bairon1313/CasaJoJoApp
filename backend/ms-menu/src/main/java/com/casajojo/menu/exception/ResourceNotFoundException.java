package com.casajojo.menu.exception;

// Excepción utilizada cuando un recurso solicitado no existe.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {

        super(message);

    }

}
