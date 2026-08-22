package com.shoppingcart;

import java.math.BigDecimal;
import java.util.Objects;

public final class CartItem {
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public CartItem(String productName, int quantity, BigDecimal unitPrice) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least one");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Unit price must not be negative");
        }
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getCartItemTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public CartItem withAdditionalQuantity(int additionalQuantity) {
        return new CartItem(productName, quantity + additionalQuantity, unitPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity
                && productName.equals(cartItem.productName)
                && unitPrice.equals(cartItem.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return quantity + " x " + productName + " @ " + unitPrice;
    }
}