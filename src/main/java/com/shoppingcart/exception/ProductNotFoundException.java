package com.shoppingcart.exception;

/** Thrown when a {@link PriceClient} has no price for the requested product. */
public class ProductNotFoundException extends PriceLookupException {

    public ProductNotFoundException(String productName) {
        super("Product not found: " + productName);
    }
}
