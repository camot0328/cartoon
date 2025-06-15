package org.example.cartoon.service;

import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;

import java.util.List;

public interface StockServiceInterface {
  List<Stock> getAllStock();
  Stock getStockByProductId(Integer productId);
  Stock saveStock(Stock stock);
  void increaseStock(Integer productId, int amount);
  void decreaseStock(Integer productId, int amount);
  void updateState(Integer productId, StockState state);
}
