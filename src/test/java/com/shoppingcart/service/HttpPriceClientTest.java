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

/** Tests for {@link HttpPriceClient} against a mocked {@link HttpClient}, so no real network call is made. */
class HttpPriceClientTest {
    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpPriceClient client = new HttpPriceClient(httpClient, URI.create("http://localhost/"));

    /** Happy path: the price field is pulled out of a 200 JSON response. */
    @Test
    void returnsThePriceFromThePriceApi() throws Exception {
        stubResponse(200, "{\"title\":\"Corn Flakes\",\"price\":2.52}");

        assertEquals(new BigDecimal("2.52"), client.getPriceByProductName("cornflakes"));
    }

    /** A non-200 response (404 here) means the product doesn't exist for this API. */
    @Test
    void rejectsAnUnknownProduct() throws Exception {
        stubResponse(404, "");

        assertThrows(ProductNotFoundException.class, () -> client.getPriceByProductName("not-a-product"));
    }

    /** If the response shape changes and drops the "price" field, mapping fails loudly instead of returning null. */
    @Test
    void rejectsAResponseMissingThePriceField() throws Exception {
        stubResponse(200, "{\"title\":\"Corn Flakes\"}");

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    /** A response body that isn't valid JSON also counts as a mapping failure. */
    @Test
    void rejectsANonJsonResponseBody() throws Exception {
        stubResponse(200, "not json");

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    /** If the connection can't be established at all, that also results in a PriceLookupException. */
    @Test
    void throwsAPriceLookupExceptionWhenTheConnectionFails() throws Exception {
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler()))
                .thenThrow(new ConnectException("Connection refused"));

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    /** A blocking send() that's interrupted mid-request also surfaces as a PriceLookupException. */
    @Test
    void throwsAPriceLookupExceptionWhenInterrupted() throws Exception {
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler()))
                .thenThrow(new InterruptedException("simulated interruption"));

        assertThrows(PriceLookupException.class, () -> client.getPriceByProductName("cornflakes"));
    }

    /** Stubs the mocked {@link HttpClient} to return the given status/body pair for any request. */
    private void stubResponse(int status, String body) throws Exception {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(status);
        when(response.body()).thenReturn(body);
        when(httpClient.send(any(HttpRequest.class), anyBodyHandler())).thenReturn(response);
    }

    /** {@code any(HttpResponse.BodyHandler.class)}, narrowed to a single suppressed cast instead of one per call site. */
    @SuppressWarnings("unchecked")
    private static HttpResponse.BodyHandler<String> anyBodyHandler() {
        return any(HttpResponse.BodyHandler.class);
    }
}
