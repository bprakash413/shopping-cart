package com.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shoppingcart.model.CartItem;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CartSummaryTest {

    @Test
    void calculatesTheExampleCartTotals() {
        CartSummary cartSummary = new CartSummary();
        List<CartItem> cartItems = List.of(
                new CartItem("cornflakes", 2, new BigDecimal("2.52")),
                new CartItem("weetabix", 1, new BigDecimal("9.98")));

        assertEquals(new BigDecimal("15.02"), cartSummary.subtotal(cartItems));
        assertEquals(new BigDecimal("1.88"), cartSummary.tax());
        assertEquals(new BigDecimal("16.90"), cartSummary.total());
    }

    @Test
    void hasZeroTotalsWhenCartIsEmpty() {
        CartSummary cartSummary = new CartSummary();

        assertEquals(new BigDecimal("0.00"), cartSummary.subtotal(List.of()));
        assertEquals(new BigDecimal("0.00"), cartSummary.tax());
        assertEquals(new BigDecimal("0.00"), cartSummary.total());
    }

    @Test
    void roundsAmountsUpToTwoDecimalPlaces() {
        CartSummary cartSummary = new CartSummary();
        List<CartItem> cartItems = List.of(new CartItem("item", 1, new BigDecimal("0.01")));

        assertEquals(new BigDecimal("0.01"), cartSummary.subtotal(cartItems));
        assertEquals(new BigDecimal("0.01"), cartSummary.tax());
        assertEquals(new BigDecimal("0.02"), cartSummary.total());
    }

    @Test
    void appliesTaxAtTwelvePointFivePercentOfSubtotal() {
        CartSummary cartSummary = new CartSummary();
        List<CartItem> cartItems = List.of(new CartItem("item", 1, new BigDecimal("100.00")));

        cartSummary.subtotal(cartItems);

        assertEquals(new BigDecimal("12.50"), cartSummary.tax());
    }

    @Test
    void sumsMultipleLineItemsForTheSubtotal() {
        CartSummary cartSummary = new CartSummary();
        List<CartItem> cartItems = List.of(
                new CartItem("cornflakes", 3, new BigDecimal("2.52")),
                new CartItem("weetabix", 2, new BigDecimal("9.98")),
                new CartItem("shreddies", 1, new BigDecimal("4.10")));

        assertEquals(new BigDecimal("31.62"), cartSummary.subtotal(cartItems));
    }
}
