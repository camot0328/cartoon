package org.example.cartoon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.cartoon.entity.*;
import org.example.cartoon.repository.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServiceInterface{
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final StockRepository stockRepository;

  @Override
  @Transactional
  public void placeOrder(Integer userId, Integer productId, Integer quantity) {
    Users user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
    Stock stock = stockRepository.findById(productId)
        .orElseThrow(() -> new IllegalArgumentException("재고 없음"));

    if (stock.getQuantity() < quantity) {
      throw new IllegalArgumentException("재고 부족");
    }

    // 주문 생성
    Order order = Order.builder()
        .user(user)
        .orderDate(LocalDateTime.now())
        .totalPrice(product.getPrice() * quantity)
        .build();
    orderRepository.save(order);

    // 주문 상세 추가
    OrderItem orderItem = new OrderItem(order, product, quantity, product.getPrice());
    orderItemRepository.save(orderItem);

    // 재고 감소
    stock.setQuantity(stock.getQuantity() - quantity);
    stockRepository.save(stock);
  }

  @Override
  public List<Order> getFilteredOrders(Integer userId, String filter) {
    LocalDateTime startDate = null;
    LocalDateTime endDate = LocalDateTime.now();

    if ("month".equals(filter)) {
      startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
    } else if ("week".equals(filter)) {
      startDate = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
    }

    if (startDate != null) {
      return orderRepository.findByUserIdAndOrderDateBetween(userId, startDate, endDate);
    } else {
      return orderRepository.findByUserId(userId);
    }
  }

  @Override
  public List<Order> getOrdersByUserId(Integer userId) {
    return orderRepository.findByUserId(userId);
  }

  @Override
  public List<OrderItem> getSalesByProductId(Integer productId) {
    return orderItemRepository.findByProductId(productId);
  }

  @Override
  public List<Order> getAllOrders() {
    return List.of();
  }

}
