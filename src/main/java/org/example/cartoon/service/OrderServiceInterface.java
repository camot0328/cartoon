package org.example.cartoon.service;

import org.example.cartoon.entity.Order;
import org.example.cartoon.entity.OrderItem;

import java.util.List;

public interface OrderServiceInterface {
  void placeOrder(Integer userId, Integer productId, Integer quantity);
  List<Order> getFilteredOrders(Integer userId, String filter);
  List<Order> getOrdersByUserId(Integer userId);
  List<OrderItem> getSalesByProductId(Integer productId);
  List<Order> getAllOrders();
}
