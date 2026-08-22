package com.shoppingcart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public final class ShoppingCart {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.125");

    private final PriceClient priceClient;
    private final Map<String, CartItem> cartItems = new HashMap<>();

    public ShoppingCart(PriceClient priceClient) {
        if (priceClient == null) {
            throw new IllegalArgumentException("priceClient must not be null");
        }
        this.priceClient = priceClient;
    }

    /**
     * Adds a product to the cart. If the product is already in the cart,
     * its quantity is increased instead of duplicating the line, and its
     * price is not looked up again.
     */
    public void addProduct(String productName, int quantity) {
        validate(productName, quantity);

        CartItem existingCartItem = cartItems.get(productName);
        if (existingCartItem != null) {
            cartItems.put(productName, existingCartItem.withAdditionalQuantity(quantity));
            return;
        }

        BigDecimal unitPrice = priceClient.getPriceByProductName(productName);
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must not be null or negative");
        }
        cartItems.put(productName, new CartItem(productName, quantity, unitPrice));
    }

    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems.values()) {
            sum = sum.add(cartItem.getCartItemTotalPrice());
        }
        return roundUp(sum);
    }

    public BigDecimal tax() {
        return roundUp(subtotal().multiply(TAX_RATE));
    }

    public BigDecimal total() {
        return roundUp(subtotal().add(tax()));
    }

    private BigDecimal roundUp(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UP);
    }

    /** Shared validation for every operation: product name is required; quantity, when given, must be positive. */
    private void validate(String productName, Integer quantity) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("productName must not be blank");
        }
        if (quantity != null && quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
