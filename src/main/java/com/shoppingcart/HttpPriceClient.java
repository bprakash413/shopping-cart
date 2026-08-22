package com.shoppingcart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * {@link PriceClient} backed by the Equal Experts take-home-test data set, where each
 * product is a static JSON file named "{@literal <product>.json}" containing a "price" field.
 */
public final class HttpPriceClient implements PriceClient {
    private static final URI BASE_URI = URI.create("https://equalexperts.github.io/backend-take-home-test-data/");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpClient httpClient;
    private final URI baseUri;

    /** Creates a client using a default HttpClient and the real product data URI. */
    public HttpPriceClient() {
        this(HttpClient.newHttpClient(), BASE_URI);
    }

    /** Visible for tests, so a fake HttpClient/baseUri can be substituted. */
    HttpPriceClient(HttpClient httpClient, URI baseUri) {
        this.httpClient = httpClient;
        this.baseUri = baseUri;
    }

    /**
     * Fetches the product's JSON file over HTTP and reads its price field.
     *
     * @throws IllegalArgumentException if the product isn't found (non-200 response)
     * @throws IllegalStateException    if the response can't be read, or has no price field
     */
    @Override
    public BigDecimal getPriceByProductName(String productName) {
        HttpRequest request = HttpRequest.newBuilder(productUri(productName)).GET().build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Product not found: " + productName);
            }
            JsonNode priceNode = OBJECT_MAPPER.readTree(response.body()).get("price");
            if (priceNode == null) {
                throw new IllegalStateException("Price missing from product response");
            }
            return priceNode.decimalValue();
        } catch (IOException ioException) {
            throw new IllegalStateException("Could not retrieve product price", ioException);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Price request interruptedException", interruptedException);
        }
    }

    /** Product name -> "{@literal <base>/<url-encoded-name>.json}". */
    private URI productUri(String productName) {
        String encodedName = URLEncoder.encode(productName, StandardCharsets.UTF_8).replace("+", "%20");
        return baseUri.resolve(encodedName + ".json");
    }
}
