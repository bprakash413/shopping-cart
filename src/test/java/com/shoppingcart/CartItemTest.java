package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CartItemTest {
    @Test
    void calculatesTotalPriceAsUnitPriceTimesQuantity() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        assertEquals(new BigDecimal("7.56"), item.getCartItemTotalPrice());
    }

    @Test
    void allowsAZeroUnitPrice() {
        CartItem item = new CartItem("item", 1, BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, item.getCartItemTotalPrice());
    }

    @Test
    void rejectsANullProductName() {
        try {
            new CartItem(null, 1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsABlankProductName() {
        try {
            new CartItem(" ", 1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsAZeroQuantity() {
        try {
            new CartItem("cornflakes", 0, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsANegativeQuantity() {
        try {
            new CartItem("cornflakes", -1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsANullUnitPrice() {
        try {
            new CartItem("cornflakes", 1, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsANegativeUnitPrice() {
        try {
            new CartItem("cornflakes", 1, new BigDecimal("-0.01"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }
}
