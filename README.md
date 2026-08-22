# Shopping Cart

A Java shopping cart library covering cart management, tax calculation, and price lookups via HTTP.

## Modules

`com.shoppingcart` — `ShoppingCart`, `CartItem`, `PriceClient`, `HttpPriceClient`

## Capabilities

`ShoppingCart` supports `addProduct` (adds a new line, or increases the quantity of an existing line if the same product is added again at the same unit price), and computes on demand:

- **Subtotal** — sum of price × quantity across all line items
- **Tax** — charged at 12.5% on the subtotal
- **Total** — subtotal + tax

All totals are rounded up to two decimal places.

## Price API

`HttpPriceClient` retrieves prices from the Price API:

```
GET https://equalexperts.github.io/backend-take-home-test-data/{product}.json
```

The `price` field is extracted from the JSON response with a regular expression, so no JSON library is required.

## Pricing formulas

- `lineTotal = unitPrice * quantity`
- `subtotal = roundUp(sum(lineTotal))`
- `tax = roundUp(subtotal * 0.125)`
- `total = roundUp(subtotal + tax)`

### Example

Adding 2 × cornflakes @ 2.52 and 1 × weetabix @ 9.98 gives:

- Subtotal = 15.02
- Tax = 1.88
- Total = 16.90

Covered by `ShoppingCartTest`.

## Build & test

```bash
mvn test
```

- `ShoppingCartTest` — cart mutation, validation, pricing calculations
- `HttpPriceClientTest` — HTTP client, tested against a real in-process `HttpServer` (JDK `com.sun.net.httpserver`) instead of a live network call or a hand-written test double

## Requirements

- Java 17
- Maven
- JUnit 5.10.2 (test scope only)

## Assumptions & tradeoffs

- Product names are matched by exact string equality; no case-insensitive normalization or whitespace trimming beyond rejecting blank names.
- Price is looked up on every `addProduct` call, not just the first time a product is added. Adding the same product again at a different price creates a separate line item.
- Cart items are stored in a `List` and matched via a linear scan, not a map.
- No persistence: the cart only holds state in memory for the lifetime of the object.
- A single 12.5% tax rate applies to every line; no discount support.
- Rounding always rounds up (`RoundingMode.CEILING`) rather than the JDK's default half-up.
- Price lookup failures (unknown product, missing `price` field, network/IO errors) are reported as generic unchecked exceptions (`IllegalArgumentException`, `IllegalStateException`) rather than dedicated exception types.

## AI tool usage

- **Use cases**: the HTTP client (`HttpPriceClient`), tax/total calculation logic.