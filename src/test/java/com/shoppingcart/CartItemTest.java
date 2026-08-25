package com.shoppingcart;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/** Tests for {@link CartItem} construction and validation. */
class CartItemTest {

    /** The additional quantity is added on top of the current one. */
    @Test
    void withAdditionalQuantityAddsToCurrentQuantity() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        CartItem updated = item.withAdditionalQuantity(2);

        assertEquals(new CartItem("cornflakes", 5, new BigDecimal("2.52")), updated);
    }

    /** The original item stays the same after the call. */
    @Test
    void withAdditionalQuantityDoesNotChangeTheOriginal() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        CartItem updated = item.withAdditionalQuantity(2);

        assertEquals(new CartItem("cornflakes", 3, new BigDecimal("2.52")), item);
        assertNotSame(item, updated);
    }

    /** A negative additional quantity reduces the current one. */
    @Test
    void withAdditionalQuantityAcceptsANegativeAmount() {
        CartItem item = new CartItem("cornflakes", 3, new BigDecimal("2.52"));

        CartItem updated = item.withAdditionalQuantity(-1);

        assertEquals(new CartItem("cornflakes", 2, new BigDecimal("2.52")), updated);
    }

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
}
