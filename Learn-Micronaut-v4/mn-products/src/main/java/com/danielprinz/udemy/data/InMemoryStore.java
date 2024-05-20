package com.danielprinz.udemy.data;

import com.danielprinz.udemy.product.Product;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStore {

  private static final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
  private final Map<Integer, Product> products = new HashMap<>();
  private final Faker faker = new Faker();

  @PostConstruct
  public void initialise() {
    IntStream.range(0, 10).forEach(
      this::addProduct
    );
  }

  private void addProduct(int id) {
    var product = new Product(id, faker.coffee().blendName(), Product.Type.COFFEE);
    products.put(id, product);
    LOG.debug("Added Product: {}", product);
  }

  public Product addProduct(Product product) {
    products.put(product.id(), product);
    return products.get(product.id());
  }

  public Product deleteProduct(Integer id) {
    return products.remove(id);
  }

  public Map<Integer, Product> getProducts() {
    return products;
  }

}
