package com.shoppingcart.service;

import com.shoppingcart.model.CartItem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ShoppingCart {
    private final PriceClient priceClient;

    private final Map<String, CartItem> cartItems = new HashMap<>();

    public ShoppingCart(PriceClient priceClient) {
        if (priceClient == null) {
            throw new IllegalArgumentException("priceClient must not be null");
        }
        this.priceClient = priceClient;
    }

    public void addProduct(String productName, int quantity) {
        validate(productName, quantity);

        CartItem existingCartItem = cartItems.get(productName);
        if (existingCartItem != null) {
            cartItems.put(productName, existingCartItem.increaseQuantity(quantity));
            return;
        }

        BigDecimal unitPrice = priceClient.getPriceByProductName(productName);
        cartItems.put(productName, new CartItem(productName, quantity, unitPrice));
    }

    public List<CartItem> getCartItems() {
        return List.copyOf(cartItems.values());
    }

    private void validate(String productName, Integer quantity) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
