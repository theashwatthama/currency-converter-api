# Currency Converter API

A Spring Boot REST API that converts amounts between currencies using
[FreecurrencyAPI](https://freecurrencyapi.com/). The application uses
Spring's `RestClient` to call the external exchange-rate API and exposes
a documented endpoint with OpenAPI and Swagger UI.

> **Project status:** Learning project built with Java 21, Spring Boot,
> Maven, MySQL, and Spring Data JPA. The core conversion endpoint,
> logging, timestamp auditing, and Swagger UI have been implemented and
> tested locally.

## Features

-   **Currency conversion:** Convert an amount from one currency to
    another using a live rate returned by FreecurrencyAPI.
-   **REST endpoint:** Accepts source currency, target currency, and
    amount as query parameters.
-   **External API integration:** Uses Spring's `RestClient` to make
    HTTP requests to FreecurrencyAPI.
-   **Structured JSON response:** Returns the input currencies, original
    amount, and converted amount.
-   **Conversion history:** Persists successful conversions in MySQL
    using Spring Data JPA.
-   **Auditing timestamps:** Tracks record creation and last
    modification timestamps with Spring Data JPA auditing.
-   **Logging:** Logs conversion attempts, successful conversions, and
    external API failures.
-   **API documentation:** OpenAPI specification and interactive Swagger
    UI.
-   **Input validation and error handling:** Basic positive-amount
    validation and handling for unavailable exchange-rate data; further
    validation and standardized error responses can be added.

## Tech Stack

-   Java 21
-   Spring Boot
-   Spring Web (`RestClient`, REST controllers)
-   Spring Data JPA / Hibernate
-   MySQL
-   Maven
-   FreecurrencyAPI
-   OpenAPI / Swagger UI (`springdoc-openapi`)
-   IntelliJ IDEA
-   Postman (API testing)

## Architecture

``` text
Client (Postman / Swagger UI)
            |
            v
CurrencyController
            |
            v
CurrencyService
       |          |
       v          v
  RestClient   ConversionHistoryRepository
       |          |
       v          v
FreecurrencyAPI  MySQL
```

### Main packages

``` text
src/main/java/com/preet/currencyconverter/
├── config/
│   ├── RestClientConfig.java
│   ├── AuditingConfig.java
│   └── OpenApiConfig.java
├── controller/
│   └── CurrencyController.java
├── dto/
│   ├── CurrencyApiResponse.java
│   └── CurrencyConversionResponse.java
├── entity/
│   └── ConversionHistory.java
├── repository/
│   └── ConversionHistoryRepository.java
├── service/
│   └── CurrencyService.java
└── CurrencyconverterApplication.java
```

> Adjust the filenames in this tree if your local project uses different
> class names. The package shown here follows the current project
> structure: `com.preet.currencyconverter`.

## Getting Started

### Prerequisites

Install or prepare:

-   JDK 21
-   Maven (or use the Maven Wrapper included in the project)
-   MySQL Server
-   A FreecurrencyAPI account and API key
-   IntelliJ IDEA or another Java IDE

### 1. Clone the repository

Replace the placeholder URL with your GitHub repository URL:

``` bash
git clone https://github.com/<your-username>/<your-repository>.git
cd <your-repository>
```

Alternatively, download the repository ZIP and open the project folder
in IntelliJ IDEA.

### 2. Create the MySQL database

Run this SQL in MySQL or DBeaver:

``` sql
CREATE DATABASE currency_converter;
```

### 3. Configure environment variables

Keep credentials out of source control. Configure the following
environment variables in your terminal or IDE Run Configuration:

``` text
DB_PASSWORD=your_mysql_password
FREECURRENCY_API_KEY=your_freecurrency_api_key
```

Use your actual MySQL username in `application.properties` if it is not
`root`.

Example `src/main/resources/application.properties`:

``` properties
spring.application.name=currencyconverter

spring.datasource.url=jdbc:mysql://localhost:3306/currency_converter
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

freecurrency.api.key=${FREECURRENCY_API_KEY}
```

Do not commit real API keys, database passwords, or other secrets. If a
key has been committed accidentally, revoke/rotate it and remove it from
the repository history where appropriate.

### 4. Run the application

From IntelliJ IDEA, run `CurrencyconverterApplication.java`.

Or use the Maven Wrapper from the project root:

**Windows:**

``` powershell
.\mvnw.cmd spring-boot:run
```

**macOS / Linux:**

``` bash
./mvnw spring-boot:run
```

The application runs on:

``` text
http://localhost:8080
```

## API Reference

### Convert currency

**Endpoint:** `GET /convertCurrency`

**Query parameters:**

  ----------------------------------------------------------------------------
  Parameter        Type           Required       Description    Example
  ---------------- -------------- -------------- -------------- --------------
  `fromCurrency`   String         Yes            Three-letter   `INR`
                                                 source         
                                                 currency code  

  `toCurrency`     String         Yes            Three-letter   `USD`
                                                 target         
                                                 currency code  

  `units`          Number         Yes            Amount to      `500`
                                                 convert; must  
                                                 be greater     
                                                 than zero      
  ----------------------------------------------------------------------------

**Example request:**

``` http
GET http://localhost:8080/convertCurrency?fromCurrency=INR&toCurrency=USD&units=500
```

**Example response** (illustrative only; the amount changes with the
rate returned by the provider):

``` json
{
  "fromCurrency": "INR",
  "toCurrency": "USD",
  "units": 500.0,
  "convertedAmount": 5.22
}
```

The converted amount above is an example, not a guaranteed or current
exchange rate.

### Swagger UI

When the application is running, open:

``` text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

``` text
http://localhost:8080/v3/api-docs
```

Use Swagger UI to inspect the endpoint, enter query parameters, execute
the request, and view the response.

## Conversion History and Auditing

Successful conversions are stored in the `conversion_history` table.

The entity tracks:

-   `createdAt` --- automatically populated when a record is created.
-   `updatedAt` --- populated/updated when the entity is modified and
    saved, depending on the entity lifecycle.

Spring Data JPA auditing is enabled using `@EnableJpaAuditing` and
`AuditingEntityListener`. The current implementation tracks timestamps;
user attribution fields such as `createdBy` and `updatedBy` require an
`AuditorAware` implementation and a reliable user identity source.

Inspect saved records with:

``` sql
SELECT *
FROM conversion_history
ORDER BY id DESC;
```

## Logging and Error Handling

The service logs conversion attempts and successful results. It also
logs exceptions raised while calling the external exchange-rate API.

Common issues to check:

-   **401 / 403 from FreecurrencyAPI:** Check the API key and
    account/plan access.
-   **Connection or timeout errors:** Check network connectivity and the
    provider's availability.
-   **Missing exchange rate:** Confirm the currency codes and that the
    provider returns the requested currency.
-   **Database connection error:** Verify MySQL is running, the database
    exists, and credentials are correct.
-   **Port 8080 already in use:** Stop the other application using the
    port or configure a different server port.

Avoid logging API keys or other secrets.

## Security Notes

-   Store `FREECURRENCY_API_KEY` and `DB_PASSWORD` as environment
    variables.
-   Keep local secrets out of Git. Add local configuration files
    containing secrets to `.gitignore`.
-   Rotate credentials immediately if they are exposed in a public
    repository.
-   For production use, add standardized exception responses, stricter
    request validation, timeouts, rate limiting, tests, and an
    appropriate database migration strategy.

## Future Improvements

-   Add standardized JSON error responses with appropriate HTTP status
    codes.
-   Validate currency codes and amount constraints consistently.
-   Add automated unit and integration tests.
-   Use `BigDecimal` for monetary calculations and define an explicit
    rounding policy.
-   Add configurable HTTP timeouts and resilience for external API
    calls.
-   Add `createdBy` / `updatedBy` auditing if authenticated user
    identity becomes available.
-   Use Flyway or Liquibase for database schema migrations in
    production.
-   Add CI checks with GitHub Actions.

## GitHub Setup Checklist

Before pushing:

-   [ ] Confirm the application builds and runs locally.
-   [ ] Test `/convertCurrency` using Postman or Swagger UI.
-   [ ] Verify conversion history and auditing timestamps in MySQL.
-   [ ] Confirm no API key or database password is present in tracked
    files.
-   [ ] Add a `.gitignore` for IDE files, build output, and local
    secrets.
-   [ ] Replace the clone URL above with your actual repository URL
    after creating it.
-   [ ] Commit the source code, `pom.xml`, Maven Wrapper, and this
    README.

## License

No license has been specified yet. If you intend others to reuse or
distribute this project, add a license file and update this section.
