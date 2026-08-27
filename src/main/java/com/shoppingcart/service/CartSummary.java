package com.shoppingcart.service;

import com.shoppingcart.model.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class CartSummary {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.125");

    private final BigDecimal subtotal;
    private final BigDecimal tax;
    private final BigDecimal total;

    public CartSummary(List<CartItem> cartItems) {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            sum = sum.add(cartItem.getCartItemTotalPrice());
        }
        this.subtotal = roundUp(sum);
        this.tax = roundUp(subtotal.multiply(TAX_RATE));
        this.total = roundUp(subtotal.add(tax));
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    private static BigDecimal roundUp(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UP);
    }
}