package org.example.cartoon.service;

import org.example.cartoon.entity.OrderItem;

import java.util.List;

public interface OrderItemServiceInterface {

  List<OrderItem> getOrderItemsByOrderId(Integer orderId);

  List<OrderItem> getOrderItemsByUserId(Integer userId);
  void cancelOrder(Integer orderId, Integer userId);

}
