package com.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.shoppingcart.model.CartItem;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class CartSummaryTest {

    @Test
    void calculatesTheExampleCartTotals() {
        List<CartItem> cartItems = List.of(
                new CartItem("cornflakes", 2, new BigDecimal("2.52")),
                new CartItem("weetabix", 1, new BigDecimal("9.98")));

        CartSummary cartSummary = new CartSummary(cartItems);

        assertEquals(new BigDecimal("15.02"), cartSummary.getSubtotal());
        assertEquals(new BigDecimal("1.88"), cartSummary.getTax());
        assertEquals(new BigDecimal("16.90"), cartSummary.getTotal());
    }

    @Test
    void hasZeroTotalsWhenCartIsEmpty() {
        CartSummary cartSummary = new CartSummary(List.of());

        assertEquals(new BigDecimal("0.00"), cartSummary.getSubtotal());
        assertEquals(new BigDecimal("0.00"), cartSummary.getTax());
        assertEquals(new BigDecimal("0.00"), cartSummary.getTotal());
    }

    @Test
    void roundsAmountsUpToTwoDecimalPlaces() {
        List<CartItem> cartItems = List.of(new CartItem("item", 1, new BigDecimal("0.01")));

        CartSummary cartSummary = new CartSummary(cartItems);

        assertEquals(new BigDecimal("0.01"), cartSummary.getSubtotal());
        assertEquals(new BigDecimal("0.01"), cartSummary.getTax());
        assertEquals(new BigDecimal("0.02"), cartSummary.getTotal());
    }

    @Test
    void appliesTaxAtTwelvePointFivePercentOfSubtotal() {
        List<CartItem> cartItems = List.of(new CartItem("item", 1, new BigDecimal("100.00")));

        CartSummary cartSummary = new CartSummary(cartItems);

        assertEquals(new BigDecimal("12.50"), cartSummary.getTax());
    }

    @Test
    void sumsMultipleLineItemsForTheSubtotal() {
        List<CartItem> cartItems = List.of(
                new CartItem("cornflakes", 3, new BigDecimal("2.52")),
                new CartItem("weetabix", 2, new BigDecimal("9.98")),
                new CartItem("shreddies", 1, new BigDecimal("4.10")));

        CartSummary cartSummary = new CartSummary(cartItems);

        assertEquals(new BigDecimal("31.62"), cartSummary.getSubtotal());
    }
}
