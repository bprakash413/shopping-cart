package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {
    private PriceClient priceClient;

    @BeforeEach
    void setUp() {
        priceClient = mock(PriceClient.class);
        when(priceClient.getPriceByProductName(eq("cornflakes"))).thenReturn(new BigDecimal("2.52"));
        when(priceClient.getPriceByProductName(eq("weetabix"))).thenReturn(new BigDecimal("9.98"));
        when(priceClient.getPriceByProductName(eq("item"))).thenReturn(new BigDecimal("0.01"));
    }

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

    @Test
    void hasZeroTotalsWhenEmpty() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertEquals(new BigDecimal("0.00"), cart.subtotal());
        assertEquals(new BigDecimal("0.00"), cart.tax());
        assertEquals(new BigDecimal("0.00"), cart.total());
    }

    @Test
    void roundsMonetaryAmountsToTwoDecimalPlaces() {
        ShoppingCart cart = new ShoppingCart(priceClient);
        cart.addProduct("item", 1);

        assertEquals(new BigDecimal("0.01"), cart.subtotal());
        assertEquals(new BigDecimal("0.01"), cart.tax());
        assertEquals(new BigDecimal("0.02"), cart.total());
    }

    @Test
    void rejectsAnInvalidQuantity() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        try {
            cart.addProduct("cornflakes", 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void rejectsABlankProductName() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        try {
            cart.addProduct(" ", 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
    }
}
