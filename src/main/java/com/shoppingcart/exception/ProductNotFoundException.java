package com.shoppingcart.exception;

public class ProductNotFoundException extends PriceLookupException {

    public ProductNotFoundException(String productName) {
        super("Product not found: " + productName);
    }
}
