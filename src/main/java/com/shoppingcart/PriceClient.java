package com.shoppingcart;

import java.math.BigDecimal;

/** Looks up the current unit price for a product, e.g. from a catalog service. */
public interface PriceClient {

    /**
     * @param productName the exact product name to look up
     * @return the current unit price
     */
    BigDecimal getPriceByProductName(String productName);
}
