package com.highway2urhell.web.rest.errors;

public class V1ApiTokenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public V1ApiTokenException() {
        super("Token null or incorrect");
    }
}
