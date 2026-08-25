package com.shoppingcart.exception;

/**
 * Thrown when a {@link PriceClient} cannot determine a product's price, e.g. because the
 * price data couldn't be reached, parsed, or was missing the expected fields.
 */
public class PriceLookupException extends RuntimeException {

    public PriceLookupException(String message) {
        super(message);
    }

    public PriceLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
