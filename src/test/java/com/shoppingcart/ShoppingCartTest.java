package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ShoppingCart} covering total calculations and input validation. */
class ShoppingCartTest {
    private PriceClient priceClient;

    /** Stubs a fake {@link PriceClient} with the prices used across these tests. */
    @BeforeEach
    void setUp() {
        priceClient = mock(PriceClient.class);
        when(priceClient.getPriceByProductName("cornflakes")).thenReturn(new BigDecimal("2.52"));
        when(priceClient.getPriceByProductName("weetabix")).thenReturn(new BigDecimal("9.98"));
        when(priceClient.getPriceByProductName("item")).thenReturn(new BigDecimal("0.01"));
    }

    /** Matches the worked example in the README: 2 x cornflakes + 1 x weetabix. */
    @Test
    void calculatesTheExampleCartTotals() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        cart.addProduct("cornflakes", 1);
        cart.addProduct("cornflakes", 1);
        cart.addProduct("weetabix", 1);

        assertEquals(new BigDecimal("15.02"), cart.subtotal());
        assertEquals(new BigDecimal("1.88"), cart.tax());
        assertEquals(new BigDecimal("16.90"), cart.total());
    }

    /** An empty cart should report zero everywhere rather than throwing. */
    @Test
    void hasZeroTotalsWhenEmpty() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertEquals(new BigDecimal("0.00"), cart.subtotal());
        assertEquals(new BigDecimal("0.00"), cart.tax());
        assertEquals(new BigDecimal("0.00"), cart.total());
    }

    /** A 0.01 unit price forces rounding up at every stage, not just the subtotal. */
    @Test
    void roundsAmountsToTwoDecimalPlaces() {
        ShoppingCart cart = new ShoppingCart(priceClient);
        cart.addProduct("item", 1);

        assertEquals(new BigDecimal("0.01"), cart.subtotal());
        assertEquals(new BigDecimal("0.01"), cart.tax());
        assertEquals(new BigDecimal("0.02"), cart.total());
    }

    /** Zero (or negative) quantity is not a valid line item. */
    @Test
    void rejectsAnInvalidQuantity() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertThrows(IllegalArgumentException.class, () -> cart.addProduct("cornflakes", 0));
    }

    /** Whitespace-only names should be treated the same as a blank name. */
    @Test
    void rejectsABlankProductName() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(" ", 1));
    }
}
