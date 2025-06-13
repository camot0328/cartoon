package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.dto.ProductFormDto;
import org.example.cartoon.entity.*;
import org.example.cartoon.repository.StockRepository;
import org.example.cartoon.service.ProductServiceImpl;
import org.example.cartoon.service.UserServiceInterface;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductServiceImpl productService;
  private final UserServiceInterface userService;
  private final StockRepository stockRepository;

  @GetMapping("/list")
  public String showProductList(Model model) {
    List<Product> products = productService.getVisibleProducts(); // 삭제 제외
    model.addAttribute("products", products);
    return "product/productList";
  }

  @GetMapping("/info/{id}")
  public String info(@PathVariable Integer id, Model model) {
    Product product = productService.getProductById(id);
    model.addAttribute("product", product);
    return "product/productInfo"; // → Thymeleaf 뷰 템플릿
  }

  @GetMapping("/search")
  public String search(@RequestParam String keyword, Model model) {
    model.addAttribute("products", productService.searchProducts(keyword));
    return "product/productList";
  }

  @GetMapping("/add")
  public String showProductForm(Model model) {
    model.addAttribute("productForm", new ProductFormDto());
    model.addAttribute("formTitle", "상품 등록");
    model.addAttribute("formAction", "/product/add");
    return "product/productForm";
  }

  @PostMapping("/add")
  public String submitProductForm(@ModelAttribute ProductFormDto productFormDto) {
    Product product = productFormDto.toEntity();
    productService.saveProduct(product);
    return "redirect:/product/list";
  }

  @GetMapping("/update/{id}")
  public String showUpdateForm(@PathVariable Integer id, Model model) {
    Product product = productService.getProductById(id); // DB에서 상품 찾기
    ProductFormDto dto = ProductFormDto.from(product); // DTO로 변환

    model.addAttribute("productForm", dto);
    model.addAttribute("formTitle", "상품 수정");
    model.addAttribute("formAction", "/product/update");

    return "product/productForm"; // 등록과 같은 템플릿 재사용
  }

  @PostMapping("/update")
  public String updateProduct(@ModelAttribute ProductFormDto productFormDto) {
    // 1. 기존 상품 조회
    Product product = productService.getProductById(productFormDto.getId());

    // 2. DTO로부터 수정값 덮어쓰기
    Product updated = Product.builder()
        .id(product.getId()) // 기존 ID 유지
        .title(productFormDto.getTitle())
        .author(productFormDto.getAuthor())
        .publisher(productFormDto.getPublisher())
        .publishDate(productFormDto.getPublishDate())
        .price(productFormDto.getPrice())
        .thumbnailImg(productFormDto.getThumbnailImg())
        .detailImg(productFormDto.getDetailImg())
        .build();

    // 3. 저장
    productService.updateProduct(updated);

    // 4. 목록 페이지로 리다이렉트
    return "redirect:/product/list";
  }

}
