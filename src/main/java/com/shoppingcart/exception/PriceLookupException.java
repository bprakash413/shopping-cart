package com.shoppingcart.exception;

public class PriceLookupException extends RuntimeException {

    public PriceLookupException(String message) {
        super(message);
    }

    public PriceLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
