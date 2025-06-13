package org.example.cartoon.dto;

import lombok.Data;
import org.example.cartoon.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
  private Integer orderId;
  private LocalDateTime orderDate;
  private Integer totalPrice;
  private List<String> productTitles;

  public static OrderResponseDto from(Order order) {
    OrderResponseDto dto = new OrderResponseDto();
    dto.setOrderId(order.getId());
    dto.setOrderDate(order.getOrderDate());
    dto.setTotalPrice(order.getTotalPrice());
    dto.setProductTitles(order.getOrderItems().stream()
        .map(item -> item.getProduct().getTitle())
        .toList());
    return dto;
  }
}
