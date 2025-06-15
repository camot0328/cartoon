package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Order;
import org.example.cartoon.entity.OrderItem;
import org.example.cartoon.entity.Users;
import org.example.cartoon.service.CartServiceInterface;
import org.example.cartoon.service.OrderItemServiceInterface;
import org.example.cartoon.service.OrderServiceInterface;
import org.example.cartoon.service.UserServiceInterface;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final CartServiceInterface cartService;
  private final OrderItemServiceInterface orderItemService;
  private final UserServiceInterface userService;
  private final OrderServiceInterface orderService;

  @GetMapping("/orders")
  public String showOrderList(@AuthenticationPrincipal User userDetails,
                              @RequestParam(value = "filter", defaultValue = "all") String filter,
                              Model model) {
    String userid = userDetails.getUsername();
    Users user = userService.findByUserid(userid);

    List<Order> orders = orderService.getFilteredOrders(user.getId(), filter);

    model.addAttribute("orders", orders);
    model.addAttribute("filter", filter);

    return "mypage/orders";
  }

  @GetMapping("/orders/{orderId}")
  public String showOrderDetail(@PathVariable Integer orderId,
                                @AuthenticationPrincipal User userDetails,
                                Model model) {

    String userid = userDetails.getUsername();
    Users users = userService.findByUserid(userid);

    // 💡 주문 항목 불러오기
    List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);

    // ❗ 본인의 주문이 아닌 경우 접근 제한 (선택사항)
    if (orderItems.isEmpty() || !orderItems.get(0).getOrder().getUser().getId().equals(users.getId())) {
      throw new AccessDeniedException("해당 주문에 접근할 수 없습니다.");
    }

    model.addAttribute("orderItems", orderItems);
    model.addAttribute("orderId", orderId);

    return "mypage/orderInfo";  // 📄 이 템플릿 만들어야 됨
  }

  @PostMapping("/orders/{orderId}/cancel")
  public String cancelOrder(@PathVariable Integer orderId,
                            @AuthenticationPrincipal User userDetails,
                            RedirectAttributes redirectAttributes) {

    String userid = userDetails.getUsername();
    Users users = userService.findByUserid(userid);

    try {
      orderItemService.cancelOrder(orderId, users.getId());
      redirectAttributes.addFlashAttribute("message", "주문이 성공적으로 취소되었습니다.");
    } catch (IllegalStateException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "주문 취소 중 오류가 발생했습니다.");
    }

    return "redirect:/mypage/orders";
  }

  @GetMapping("/cart")
  public String showCartPage(@AuthenticationPrincipal User userDetails,
                             Model model) {
    String userid = userDetails.getUsername();
    Users users = userService.findByUserid(userid);

    log.info("장바구니 조회 - 사용자: {}", users.getUserid());

    model.addAttribute("cartItems", cartService.getCartItems(users.getId()));
    return "mypage/cart";
  }

  // ✅ 수량 변경
  @PostMapping("/cart/update")
  public String updateQuantity(@AuthenticationPrincipal User userDetails,
                               @RequestParam("productId") Integer productId,
                               @RequestParam("quantity") Integer quantity) {
    String userid = userDetails.getUsername(); // 로그인 ID
    Users user = userService.findByUserid(userid); // Users 객체 가져오기
    cartService.updateQuantity(user.getId(), productId, quantity); // Integer id 전달
    log.info("수량 변경 - 사용자: {}, 상품: {}, 수량: {}", userid, productId, quantity);
    return "redirect:/mypage/cart";
  }

  // ✅ 항목 삭제
  @PostMapping("/cart/delete")
  public String deleteCartItem(@AuthenticationPrincipal User userDetails,
                               @RequestParam("productId") Integer productId) {
    String userid = userDetails.getUsername();
    Users user = userService.findByUserid(userid);
    cartService.removeItem(user.getId(), productId);
    log.info("장바구니 항목 삭제 - 사용자: {}, 상품: {}", userid, productId);
    return "redirect:/mypage/cart";
  }

  @GetMapping("/profile")
  public String showProfile(@AuthenticationPrincipal User userDetails, Model model) {
    Users user = userService.findByUserid(userDetails.getUsername());
    model.addAttribute("user", user);
    return "mypage/profile";
  }

  @PostMapping("/profile")
  public String updateProfile(@AuthenticationPrincipal User userDetails,
                              @RequestParam String nickname,
                              @RequestParam String currentPassword,
                              @RequestParam(required = false) String newPassword,
                              @RequestParam(required = false) String confirmPassword,
                              Model model) {

    String userid = userDetails.getUsername();
    try {
      userService.updateProfile(userid, nickname, currentPassword, newPassword, confirmPassword);
      return "redirect:/mypage/profile?success";
    } catch (IllegalArgumentException e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("user", userService.findByUserid(userid));
      return "mypage/profile";
    }
  }
}
