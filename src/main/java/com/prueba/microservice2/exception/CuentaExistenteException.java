package com.prueba.microservice2.exception;

public class CuentaExistenteException extends RuntimeException {
    public CuentaExistenteException(String message) {
        super(message);
    }
}