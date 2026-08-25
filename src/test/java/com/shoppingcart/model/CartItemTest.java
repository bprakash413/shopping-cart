package com.shoppingcart.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class CartItemTest {

    @Test
    void increaseQuantityAddsToCurrentQuantity() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        CartItem updated = item.increaseQuantity(2);

        assertEquals(new CartItem("cornflakes", 5, new BigDecimal("2.52")), updated);
    }

    @Test
    void increaseQuantityDoesNotChangeTheOriginal() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        CartItem updated = item.increaseQuantity(2);

        assertEquals(new CartItem("cornflakes", 3, new BigDecimal("2.52")), item);
        assertNotSame(item, updated);
    }

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
}
