package com.shoppingcart.service;

import com.shoppingcart.model.CartItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * A cart of products, keyed by product name. Prices are fetched once per product via the
 * supplied {@link PriceClient} and cached on the {@link CartItem} for the lifetime of the cart.
 */
public final class ShoppingCart {
    private static final BigDecimal TAX_RATE = new BigDecimal("0.125");

    private final PriceClient priceClient;

    /** Create a Map of productName and CartItem */
    private final Map<String, CartItem> cartItems = new HashMap<>();

    /** Creates an empty cart that looks up prices via the given client. */
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

    /** Sum of every line's total price, rounded up to 2 decimal places. */
    public BigDecimal subtotal() {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems.values()) {
            sum = sum.add(cartItem.getCartItemTotalPrice());
        }
        return roundUp(sum);
    }

    /** Tax at {@link #TAX_RATE} on the subtotal, rounded up to 2 decimal places. */
    public BigDecimal tax() {
        return roundUp(subtotal().multiply(TAX_RATE));
    }

    /** Subtotal plus tax, rounded up to 2 decimal places. */
    public BigDecimal total() {
        return roundUp(subtotal().add(tax()));
    }

    /** Rounds up to 2 decimal places. */
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
