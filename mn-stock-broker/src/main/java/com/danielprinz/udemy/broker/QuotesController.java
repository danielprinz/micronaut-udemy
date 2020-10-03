package com.danielprinz.udemy.broker;

import java.util.List;
import java.util.Optional;

import com.danielprinz.udemy.broker.model.CustomError;
import com.danielprinz.udemy.broker.model.Quote;
import com.danielprinz.udemy.broker.persistence.jpa.QuotesRepository;
import com.danielprinz.udemy.broker.persistence.model.QuoteEntity;
import com.danielprinz.udemy.broker.persistence.model.SymbolEntity;
import com.danielprinz.udemy.broker.store.InMemoryStore;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

  private final InMemoryStore store;
  private final QuotesRepository quotes;

  public QuotesController(final InMemoryStore store,
    final QuotesRepository quotes) {
    this.store = store;
    this.quotes = quotes;
  }

  @Operation(summary = "Returns a quote for the given symbol.")
  @ApiResponse(
    content = @Content(mediaType = MediaType.APPLICATION_JSON)
  )
  @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
  @Tag(name = "quotes")
  @Get("/{symbol}")
  public HttpResponse getQuotes(@PathVariable String symbol) {
    final Optional<Quote> maybeQuote = store.fetchQuote(symbol);
    if (maybeQuote.isEmpty()) {
      final CustomError notFound = CustomError.builder()
        .status(HttpStatus.NOT_FOUND.getCode())
        .error(HttpStatus.NOT_FOUND.name())
        .message("quote for symbol not available")
        .path("/quotes/" + symbol)
        .build();
      return HttpResponse.notFound(notFound);
    }
    return HttpResponse.ok(maybeQuote.get());
  }

  @Get("/jpa")
  public List<QuoteEntity> getAllQuotesViaJPA() {
    return quotes.findAll();
  }

  @Operation(summary = "Returns a quote for the given symbol. Fetched from the database via JPA")
  @ApiResponse(
    content = @Content(mediaType = MediaType.APPLICATION_JSON)
  )
  @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
  @Tag(name = "quotes")
  @Get("/{symbol}/jpa")
  public HttpResponse getQuoteViaJPA(@PathVariable String symbol) {
    final Optional<QuoteEntity> maybeQuote = quotes.findBySymbol(new SymbolEntity(symbol));
    if (maybeQuote.isEmpty()) {
      final CustomError notFound = CustomError.builder()
        .status(HttpStatus.NOT_FOUND.getCode())
        .error(HttpStatus.NOT_FOUND.name())
        .message("quote for symbol not available in db.")
        .path("/quotes/" + symbol + "/jpa")
        .build();
      return HttpResponse.notFound(notFound);
    }
    return HttpResponse.ok(maybeQuote.get());
  }
}
