package org.example.cartoon.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemId implements Serializable {
  private Integer cart;
  private Integer product;
}