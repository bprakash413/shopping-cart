package com.shoppingcart.service;

import com.shoppingcart.model.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CartSummary {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.125");

    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;

    public BigDecimal subtotal(List<CartItem> cartItemList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            sum = sum.add(cartItem.getCartItemTotalPrice());
        }
        subtotal = roundUp(sum);
        return subtotal;
    }

    public BigDecimal tax() {
        tax = roundUp(subtotal.multiply(TAX_RATE));
        return tax;
    }

    public BigDecimal total() {
        return roundUp(subtotal.add(tax));
    }

    private BigDecimal roundUp(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.UP);
    }
}