package com.danielprinz.udemy.admin.product;

import com.danielprinz.udemy.product.Product;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record UpdateProductRequest(
  String name,
  Product.Type type
) {
}
