package com.casajojo.menu.exception;

// Excepción utilizada cuando se incumple una regla de negocio.
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {

        super(message);

    }

}
