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

- `cartItemTotal = unitPrice * quantity`
- `subtotal = roundUp(sum(cartItemTotal))`
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

- `ShoppingCartTest` — cart, validation, pricing calculations
- `CartItemTest` — cart item total, validation of product name/quantity/unit price
- `HttpPriceClientTest` — HTTP client

## Requirements

- Java 25
- Maven
- JUnit 5.10.2 (test scope only)
- Mockito 5.19.0 (test scope only)

## Assumptions & tradeoffs

- Product names must match exactly. Different case or extra spaces are treated as a different product — for example, `"cornflakes"` and `"Cornflakes "` are two separate lines in the cart, even though both successfully look up a price.
- Nothing is saved anywhere. The cart only exists in memory while the program is running.
- Every item is taxed at a flat 12.5%.
- Totals are always rounded up, never rounded to the nearest value.
- If a price can't be looked up (product not found, price missing, or a network problem), the code throws a generic error instead of a specific one for each case.

## AI tool usage

- **What AI was used for:** writing JavaDoc comments, writing this README, and writing the `HttpPriceClientTest` test cases.
- **How much code AI wrote:** approximately 15% of the code in this project, based on JavaDoc comments and the `HttpPriceClientTest` test class.
- **How it was checked:** by running the unit tests and making sure they pass.