package com.danielprinz.udemy.auth.persistence;

import java.util.Optional;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

  Optional<UserEntity> findByEmail(String email);
}
