package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingCartTest {
    private PriceClient priceClient;

    @BeforeEach
    void setUp() {
        Map<String, BigDecimal> priceMap = new HashMap<>();
        priceMap.put("cornflakes", new BigDecimal("2.52"));
        priceMap.put("weetabix", new BigDecimal("9.98"));
        priceMap.put("item", new BigDecimal("0.01"));

        priceClient = new MockPriceClient(priceMap);
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

    private static final class MockPriceClient implements PriceClient {
        private final Map<String, BigDecimal> priceTable;

        MockPriceClient(Map<String, BigDecimal> priceTable) {
            this.priceTable = priceTable;
        }

        @Override
        public BigDecimal getPriceByProductName(String productName) {
            return priceTable.get(productName);
        }
    }
}
