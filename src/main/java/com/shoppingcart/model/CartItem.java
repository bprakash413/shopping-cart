package com.shoppingcart.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class CartItem {
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public CartItem(String productName, int quantity, BigDecimal unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCartItemTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public CartItem increaseQuantity(int quantity) {
        return new CartItem(productName, this.quantity + quantity, unitPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity
                && productName.equals(cartItem.productName)
                && unitPrice.compareTo(cartItem.unitPrice) == 0;
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