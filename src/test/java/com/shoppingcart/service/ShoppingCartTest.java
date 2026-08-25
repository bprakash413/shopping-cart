package com.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shoppingcart.model.CartItem;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {
    private PriceClient priceClient;
    private CartSummary cartSummary;

    @BeforeEach
    void setUp() {
        priceClient = mock(PriceClient.class);
        when(priceClient.getPriceByProductName("cornflakes")).thenReturn(new BigDecimal("2.52"));
        when(priceClient.getPriceByProductName("weetabix")).thenReturn(new BigDecimal("9.98"));
        when(priceClient.getPriceByProductName("item")).thenReturn(new BigDecimal("0.01"));
        cartSummary = new CartSummary();
    }

    @Test
    void calculatesTheExampleCartTotals() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        cart.addProduct("cornflakes", 1);
        cart.addProduct("cornflakes", 1);
        cart.addProduct("weetabix", 1);

        assertEquals(new BigDecimal("15.02"), cartSummary.subtotal(cart.getCartItems()));
        assertEquals(new BigDecimal("1.88"), cartSummary.tax());
        assertEquals(new BigDecimal("16.90"), cartSummary.total());
    }

    @Test
    void hasZeroTotalsWhenEmpty() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertEquals(new BigDecimal("0.00"), cartSummary.subtotal(cart.getCartItems()));
        assertEquals(new BigDecimal("0.00"), cartSummary.tax());
        assertEquals(new BigDecimal("0.00"), cartSummary.total());
    }

    @Test
    void roundsAmountsToTwoDecimalPlaces() {
        ShoppingCart cart = new ShoppingCart(priceClient);
        cart.addProduct("item", 1);

        assertEquals(new BigDecimal("0.01"), cartSummary.subtotal(cart.getCartItems()));
        assertEquals(new BigDecimal("0.01"), cartSummary.tax());
        assertEquals(new BigDecimal("0.02"), cartSummary.total());
    }

    @Test
    void rejectsAnInvalidQuantity() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertThrows(IllegalArgumentException.class, () -> cart.addProduct("cornflakes", 0));
    }

    @Test
    void rejectsABlankProductName() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        assertThrows(IllegalArgumentException.class, () -> cart.addProduct(" ", 1));
    }

    @Test
    void getCartItemsReturnsAddedProducts() {
        ShoppingCart cart = new ShoppingCart(priceClient);

        cart.addProduct("cornflakes", 2);

        assertEquals(1, cart.getCartItems().size());
        assertTrue(cart.getCartItems().contains(
                new CartItem("cornflakes", 2, new BigDecimal("2.52"))));
    }
}
