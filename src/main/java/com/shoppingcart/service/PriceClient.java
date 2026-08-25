package com.shoppingcart.service;

import com.shoppingcart.exception.PriceLookupException;
import com.shoppingcart.exception.ProductNotFoundException;
import java.math.BigDecimal;

public interface PriceClient {

    BigDecimal getPriceByProductName(String productName);
}
