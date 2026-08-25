package com.shoppingcart.service;

import com.shoppingcart.exception.PriceLookupException;
import com.shoppingcart.exception.ProductNotFoundException;
import java.math.BigDecimal;

/** Looks up the current unit price for a product, e.g. from a catalog service. */
public interface PriceClient {

    /**
     * @param productName the exact product name to look up
     * @return the current unit price
     * @throws ProductNotFoundException if the product doesn't exist
     * @throws PriceLookupException     if the price can't be determined for any other reason
     */
    BigDecimal getPriceByProductName(String productName);
}
