package org.example.cartoon.service;

import org.example.cartoon.entity.Stock;
import java.util.List;

public interface StockServiceInterface {
  List<Stock> getAllStock();
  Stock getStockByProductId(Integer productId);
  Stock saveStock(Stock stock);
}
