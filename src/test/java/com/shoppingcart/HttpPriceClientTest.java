package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;

class HttpPriceClientTest {
    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpPriceClient client = new HttpPriceClient(httpClient, URI.create("http://localhost/"));

    @Test
    void returnsThePriceFromThePriceApi() throws Exception {
        stubResponse(200, "{\"title\":\"Corn Flakes\",\"price\":2.52}");

        assertEquals(new BigDecimal("2.52"), client.getPriceByProductName("cornflakes"));
    }

    @Test
    void rejectsAnUnknownProduct() throws Exception {
        stubResponse(404, "");

        assertThrows(IllegalArgumentException.class, () -> client.getPriceByProductName("not-a-product"));
    }

    @SuppressWarnings("unchecked")
    private void stubResponse(int status, String body) throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status);
        when(response.body()).thenReturn(body);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);
    }
}
