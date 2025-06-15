package org.example.cartoon.repository;

import org.example.cartoon.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
  List<Order> findByUserId(Integer userId);

  List<Order> findByUserIdAndOrderDateBetween(Integer userId, LocalDateTime start, LocalDateTime end);

}
