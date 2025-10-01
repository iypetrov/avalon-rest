package org.avalon.avalonrest.products;

public class UnknownCurrencyException extends RuntimeException {
    public UnknownCurrencyException(String message) {
        super(message);
    }
}