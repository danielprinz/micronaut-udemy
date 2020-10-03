package com.danielprinz.udemy.broker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError {
  private int status;
  private String error;
  private String message;
  private String path;
}
