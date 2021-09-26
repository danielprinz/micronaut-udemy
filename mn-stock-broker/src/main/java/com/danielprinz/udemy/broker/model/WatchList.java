package com.danielprinz.udemy.broker.model;

import java.util.ArrayList;
import java.util.List;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Introspected
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {

  private List<Symbol> symbols = new ArrayList<>();

}
