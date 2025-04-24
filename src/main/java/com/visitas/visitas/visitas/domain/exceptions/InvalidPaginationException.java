package com.visitas.visitas.visitas.domain.exceptions;

public class InvalidPaginationException extends RuntimeException {
    public InvalidPaginationException(String message) {
        super(message);
    }
}
