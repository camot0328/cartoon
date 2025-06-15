package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Order;
import org.example.cartoon.entity.Users;
import org.example.cartoon.service.OrderServiceInterface;
import org.example.cartoon.service.UserServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

  private final OrderServiceInterface orderService;
  private final UserServiceInterface userService;

  @GetMapping
  public String showOrders(@RequestParam(required = false) String userid, Model model) {
    List<Order> orders;

    if (userid != null && !userid.isBlank()) {
      Users user = userService.findByUserid(userid);
      orders = orderService.getOrdersByUserId(user.getId());
      model.addAttribute("search", userid);
    } else {
      orders = orderService.getAllOrders();
    }

    model.addAttribute("orders", orders);
    return "admin/orderList";
  }
}
