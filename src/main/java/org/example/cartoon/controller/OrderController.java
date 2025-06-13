package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Users;
import org.example.cartoon.service.OrderServiceInterface;
import org.example.cartoon.service.UserServiceInterface;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private final OrderServiceInterface orderService;
  private final UserServiceInterface userService;

  @PostMapping("/add")
  public String placeOrder(@AuthenticationPrincipal User userDetails,
                           @RequestParam("productId") Integer productId,
                           @RequestParam("quantity") Integer quantity) {

    String userid = userDetails.getUsername();
    Users user = userService.findByUserid(userid);

    log.info("바로 구매 요청 - 사용자: {}, 상품 ID: {}, 수량: {}", userid, productId, quantity);

    try {
      orderService.placeOrder(user.getId(), productId, quantity);
    } catch (Exception e) {
      log.error("바로 구매 실패: {}", e.getMessage());
      return "redirect:/mypage/cart?error=주문실패";
    }

    return "redirect:/mypage/orders"; // 주문 내역 페이지로 이동
  }
}
