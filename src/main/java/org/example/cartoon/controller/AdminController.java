package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Order;
import org.example.cartoon.entity.OrderItem;
import org.example.cartoon.entity.Product;
import org.example.cartoon.entity.Users;
import org.example.cartoon.service.OrderServiceInterface;
import org.example.cartoon.service.ProductServiceInterface;
import org.example.cartoon.service.UserServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // 🔐 전체 컨트롤러에 관리자 권한 적용
public class AdminController {

  private final UserServiceInterface userService;
  private final ProductServiceInterface productService;
  private final OrderServiceInterface orderService;

  // 1. 관리자 대시보드
  @GetMapping
  public String showDashboard() {
    return "admin/admin"; // templates/admin/admin.html
  }

  // 2. 검색 기능 (회원 or 상품)
  @GetMapping("/search")
  public String search(@RequestParam String type,
                       @RequestParam String keyword,
                       Model model) {

    if (type.equals("user")) {
      // 유저 검색 + 주문내역
      Users user = userService.findByUserid(keyword);
      if (user == null) {
        model.addAttribute("error", "해당 회원을 찾을 수 없습니다.");
        return "admin/admin";
      }
      List<Order> orders = orderService.getOrdersByUserId(user.getId());
      model.addAttribute("user", user);
      model.addAttribute("orders", orders);
      return "admin/search-user";
    }

    if (type.equals("product")) {
      // 상품 검색 + 판매이력
      Product product = productService.findByTitle(keyword);
      if (product == null) {
        model.addAttribute("error", "해당 상품을 찾을 수 없습니다.");
        return "admin/admin";
      }
      List<OrderItem> sales = orderService.getSalesByProductId(product.getId());
      model.addAttribute("product", product);
      model.addAttribute("sales", sales);
      return "admin/search-product";
    }

    model.addAttribute("error", "잘못된 검색 유형입니다.");
    return "admin/admin";
  }

  // 관리자 상품 목록 보기
  @GetMapping("/products")
  public String showAllProducts(Model model) {
    List<Product> products = productService.getAllForAdmin();
    model.addAttribute("products", products);
    return "admin/product-list";
  }

  // 상품 소프트 삭제
  @PostMapping("/products/delete/{id}")
  public String softDeleteProduct(@PathVariable Integer id) {
    productService.softDeleteProduct(id);
    return "redirect:/admin/products";
  }

  // 상품 복원
  @PostMapping("/products/restore/{id}")
  public String restoreProduct(@PathVariable Integer id) {
    productService.restoreProduct(id);
    return "redirect:/admin/products";
  }

  @GetMapping("/users")
  public String showAllUsers(Model model) {
    List<Users> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "admin/adminUserList";
  }

  // 회원 상세 보기
  @GetMapping("/users/{id}")
  public String showUserDetail(@PathVariable Integer id, Model model) {
    Users user = userService.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
    List<Order> orders = orderService.getOrdersByUserId(id);
    model.addAttribute("user", user);
    model.addAttribute("orders", orders);
    return "admin/adminUserDetail";
  }
}
