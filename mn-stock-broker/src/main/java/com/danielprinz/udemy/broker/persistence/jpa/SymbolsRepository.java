package com.danielprinz.udemy.broker.persistence.jpa;

import java.util.List;

import com.danielprinz.udemy.broker.persistence.model.SymbolEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.data.repository.GenericRepository;

@Repository
public interface SymbolsRepository extends CrudRepository<SymbolEntity, Integer> {

  List<SymbolEntity> findAll();

}
