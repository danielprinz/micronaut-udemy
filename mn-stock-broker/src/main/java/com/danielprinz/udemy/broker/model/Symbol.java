package com.danielprinz.udemy.broker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Symbol", description = "Abbreviation to uniquely identify public traded shares of a stock.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {

  @Schema(description = "symbol value", minLength = 1, maxLength = 5)
  private String value;
}
