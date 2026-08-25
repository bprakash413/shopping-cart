package com.shoppingcart.model;

import com.shoppingcart.service.ShoppingCart;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * This class contains {@link ShoppingCart} Single Line Item which contains product, quantity and price per unit.<br/>
 * Immutable - quantity changes will go through {@link #withAdditionalQuantity(int)}
 */
public final class CartItem {
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    /**
     * Creates a cart item.
     *
     * @throws IllegalArgumentException if productName is blank, quantity is less than one,
     *                                   or unitPrice is null/negative
     */
    public CartItem(String productName, int quantity, BigDecimal unitPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /** Quantity times unit price, unrounded. */
    public BigDecimal getCartItemTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /** Returns a new item with the extra quantity added on top of the current one. */
    public CartItem withAdditionalQuantity(int additionalQuantity) {
        return new CartItem(productName, quantity + additionalQuantity, unitPrice);
    }

    /** Two cart items are equal when product name, quantity, and unit price all match. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return quantity == cartItem.quantity
                && productName.equals(cartItem.productName)
                && unitPrice.compareTo(cartItem.unitPrice) == 0;
    }

    /** Consistent with {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        return Objects.hash(productName, quantity, unitPrice);
    }

    /** Returns a readable "2 x Apple @ 2.52" style summary of this line. */
    @Override
    public String toString() {
        return quantity + " x " + productName + " @ " + unitPrice;
    }
}