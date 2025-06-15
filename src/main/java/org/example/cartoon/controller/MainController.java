package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import org.example.cartoon.entity.Product;
import org.example.cartoon.service.ProductServiceInterface;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
  private final ProductServiceInterface productService;

  @GetMapping("/home")
  public String index(Model model) {
    List<Product> latestBooks = productService.getLatestProducts();
    model.addAttribute("products", latestBooks);
    return "index"; // src/main/resources/templates/index.html
  }

  @Controller
  public class AuthController {

    @GetMapping("/login")
    public String loginForm() {
      return "login"; // src/main/resources/templates/login.html
    }
  }

  @GetMapping("/search")
  public String searchProducts(@RequestParam("query") String keyword,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               Model model) {
    int size = 15; // 한 페이지당 15개

    Page<Product> resultPage = productService.searchByKeywordWithStockPaged(keyword, page, size);

    model.addAttribute("products", resultPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", resultPage.getTotalPages());
    model.addAttribute("keyword", keyword);
    return "product/productList";
  }
}
