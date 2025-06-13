package org.example.cartoon.repository;

import org.example.cartoon.entity.Product;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {

  Optional<Stock> findByProduct(Product product);

  List<Stock> findByStateNot(StockState state); // 삭제 제외용
}
