package com.danielprinz.udemy.auth.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotEmpty
  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @NotEmpty
  private String password;


}
