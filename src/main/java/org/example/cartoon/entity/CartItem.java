package org.example.cartoon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(CartItemId.class)
public class CartItem {

  @Id
  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @Id
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer quantity;

}

