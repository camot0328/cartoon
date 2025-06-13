package org.example.cartoon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(OrderItemId.class)
public class OrderItem {
  @Id
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @Id
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer quantity;
  private Integer price;
}
