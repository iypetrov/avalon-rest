package org.avalon.avalonrest.products;

public class UnknownProductTypeException extends RuntimeException {
    public UnknownProductTypeException(String message) {
        super(message);
    }
}