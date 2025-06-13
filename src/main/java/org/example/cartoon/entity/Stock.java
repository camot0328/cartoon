package org.example.cartoon.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Stock {
  @Id
  @Column(name = "product_id")
  private Integer productId;

  @MapsId
  @OneToOne
  @JoinColumn(name = "product_id")
  private Product product;

  private Integer quantity;

  @Enumerated(EnumType.STRING)
  private StockState state;
}