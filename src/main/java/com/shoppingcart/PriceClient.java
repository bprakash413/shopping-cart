package com.shoppingcart;

import java.math.BigDecimal;

public interface PriceClient {
    BigDecimal getPriceByProductName(String productName);
}
