package org.example.cartoon.dto;

import lombok.Data;
import org.example.cartoon.entity.Product;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;

@Data
public class StockDto {
  private Integer productId;
  private Integer quantity;
  private StockState state;

  public Stock toEntity(Product product) {
    Stock stock = new Stock();
    stock.setProduct(product);
    stock.setQuantity(this.quantity);
    stock.setState(this.state);
    return stock;
  }
}
