package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;
import org.example.cartoon.repository.ProductRepository;
import org.example.cartoon.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockServiceInterface {

  private final StockRepository stockRepository;
  private final ProductRepository productRepository;

  @Override
  public List<Stock> getAllStock() {
    List<Stock> list = stockRepository.findAll();
    log.info("전체 재고 조회: {}", list); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return list;
  }

  @Override
  public Stock getStockByProductId(Integer productId) {
    Stock stock = stockRepository.findById(productId).orElse(null);
    log.info("상품 재고 조회 (productId={}): {}", productId, stock); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return stock;
  }

  @Override
  public Stock saveStock(Stock stock) {
    Stock saved = stockRepository.save(stock);
    log.info("재고 저장: {}", saved); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return saved;
  }

  @Override
  @Transactional
  public void increaseStock(Integer productId, int amount) {
    Stock stock = stockRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고가 없습니다."));
    stock.setQuantity(stock.getQuantity() + amount);
    stockRepository.save(stock);
  }

  @Override
  @Transactional
  public void decreaseStock(Integer productId, int amount) {
    Stock stock = stockRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고가 없습니다."));
    int updated = stock.getQuantity() - amount;
    if (updated < 0) throw new IllegalArgumentException("재고 수량은 0 미만이 될 수 없습니다.");
    stock.setQuantity(updated);
    stockRepository.save(stock);
  }

  @Override
  @Transactional
  public void updateState(Integer productId, StockState state) {
    Stock stock = stockRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고가 없습니다."));
    stock.setState(state);
    stockRepository.save(stock);
  }
}
