package org.example.cartoon.repository;

import org.example.cartoon.entity.OrderItem;
import org.example.cartoon.entity.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

  /**
   * 특정 주문 ID에 대한 주문 항목들 조회
   */
  List<OrderItem> findByOrderId(Integer orderId);

  /**
   * 특정 사용자 ID로 주문 항목 조회 (선택적으로 확장 가능)
   */
  List<OrderItem> findByOrderUserId(Integer userId);

  void deleteByOrderId(Integer orderId);
  List<OrderItem> findByProductId(Integer productId);

}
