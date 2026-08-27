package com.shoppingcart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.shoppingcart.exception.PriceLookupException;
import com.shoppingcart.exception.ProductNotFoundException;
import java.math.BigDecimal;
import java.net.ConnectException;
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

        assertThrows(ProductNotFoundException.class, () -> client.getPriceByProductName("not-a-product"));
    }

    @Test
    void rejectsAResponseMissingThePriceField() throws Exception {
        stubResponse(200, "{\"title\":\"Corn Flakes\"}");

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    @Test
    void rejectsANonNumericPriceField() throws Exception {
        stubResponse(200, "{\"price\":\"free\"}");

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    @Test
    void rejectsAServerErrorWithAGenericPriceLookupException() throws Exception {
        stubResponse(500, "");

        PriceLookupException exception = assertThrows(PriceLookupException.class,
                () -> client.getPriceByProductName("cornflakes"));
        assertEquals(PriceLookupException.class, exception.getClass());
    }

    @Test
    void rejectsANonJsonResponseBody() throws Exception {
        stubResponse(200, "not json");

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    @Test
    void throwsAPriceLookupExceptionWhenTheConnectionFails() throws Exception {
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler()))
                .thenThrow(new ConnectException("Connection refused"));

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    @Test
    void throwsAPriceLookupExceptionWhenInterrupted() throws Exception {
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler()))
                .thenThrow(new InterruptedException("simulated interruption"));

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    private void stubResponse(int status, String body) throws Exception {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status);
        when(response.body()).thenReturn(body);
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler())).thenReturn(response);
    }

    @SuppressWarnings("unchecked")
    private static HttpResponse.BodyHandler<String> anyBodyHandler() {
        return any(HttpResponse.BodyHandler.class);
    }
}
