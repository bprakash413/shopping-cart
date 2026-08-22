package com.shoppingcart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

class HttpPriceClientTest {
    @Test
    void returnsThePriceFromThePriceApi() throws Exception {
        HttpServer server = serverWithResponse(200, "{\"title\":\"Corn Flakes\",\"price\":2.52}");
        try {
            PriceClient client = clientFor(server);
            assertEquals(new BigDecimal("2.52"), client.getPriceByProductName("cornflakes"));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void rejectsAnUnknownProduct() throws Exception {
        HttpServer server = serverWithResponse(404, "");
        try {
            try {
                clientFor(server).getPriceByProductName("not-a-product");
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException expected) {
            }
        } finally {
            server.stop(0);
        }
    }

    private HttpPriceClient clientFor(HttpServer server) {
        int port = server.getAddress().getPort();
        return new HttpPriceClient(HttpClient.newHttpClient(), URI.create("http://localhost:" + port + "/"));
    }

    private HttpServer serverWithResponse(int status, String body) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                byte[] response = body.getBytes();
                exchange.sendResponseHeaders(status, response.length);
                exchange.getResponseBody().write(response);
                exchange.close();
            }
        });
        server.start();
        return server;
    }
}
