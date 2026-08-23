package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Tests for {@link CartItem} construction and validation. */
class CartItemTest {
    /** Basic unit price x quantity math. */
    @Test
    void calculatesTotalPriceAsUnitPriceTimesQuantity() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        assertEquals(new BigDecimal("7.56"), item.getCartItemTotalPrice());
    }

    /** Free items are a valid line item, not an error. */
    @Test
    void allowsAZeroUnitPrice() {
        CartItem item = new CartItem("item", 1, BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, item.getCartItemTotalPrice());
    }

    /** A product name is required. */
    @Test
    void rejectsANullProductName() {
        try {
            new CartItem(null, 1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Whitespace-only counts as blank. */
    @Test
    void rejectsABlankProductName() {
        try {
            new CartItem(" ", 1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Quantity must be at least one. */
    @Test
    void rejectsAZeroQuantity() {
        try {
            new CartItem("cornflakes", 0, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Same as a zero quantity - still not a valid line item. */
    @Test
    void rejectsANegativeQuantity() {
        try {
            new CartItem("cornflakes", -1, new BigDecimal("1.00"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** A unit price is required to compute the line total. */
    @Test
    void rejectsANullUnitPrice() {
        try {
            new CartItem("cornflakes", 1, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    /** Unlike quantity, a zero unit price is fine - only negative is rejected. */
    @Test
    void rejectsANegativeUnitPrice() {
        try {
            new CartItem("cornflakes", 1, new BigDecimal("-0.01"));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }
}
