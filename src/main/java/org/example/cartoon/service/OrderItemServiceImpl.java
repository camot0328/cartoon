package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Order;
import org.example.cartoon.entity.OrderItem;
import org.example.cartoon.repository.OrderItemRepository;
import org.example.cartoon.repository.OrderRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemServiceInterface {

  private final OrderItemRepository orderItemRepository;
  private final OrderRepository orderRepository;

  @Override
  public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
    log.info("주문 ID {} 에 대한 주문 항목 조회", orderId);
    return orderItemRepository.findByOrderId(orderId);
  }

  @Override
  public List<OrderItem> getOrderItemsByUserId(Integer userId) {
    log.info("사용자 ID {} 의 주문 항목 조회", userId);
    return orderItemRepository.findByOrderUserId(userId);
  }

  @Override
  public void cancelOrder(Integer orderId, Integer userId) {
    // 주문 조회
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

    // 사용자 확인
    if (!order.getUsers().getId().equals(userId)) {
      throw new AccessDeniedException("해당 주문에 접근할 수 없습니다.");
    }

    // 주문일 기준 3일 이내인지 체크
    LocalDateTime orderDate = order.getOrderDate();
    if (orderDate.plusDays(3).isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("주문 후 3일이 지나서 취소할 수 없습니다.");
    }

    // 주문 취소 - 하드 삭제
    orderItemRepository.deleteByOrderId(orderId);
    orderRepository.deleteById(orderId);
  }

}
