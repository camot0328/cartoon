package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;
import org.example.cartoon.service.ProductServiceInterface;
import org.example.cartoon.service.StockServiceInterface;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/stocks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminStockController {

  private final StockServiceInterface stockService;
  private final ProductServiceInterface productService;

  @GetMapping
  public String showStockList(Model model) {
    List<Stock> stockList = stockService.getAllStock();
    model.addAttribute("stocks", stockList);
    return "admin/stockList";
  }

  @PostMapping("/increase/{productId}")
  public String increaseStock(@PathVariable Integer productId,
                              @RequestParam Integer amount) {
    stockService.increaseStock(productId, amount);
    return "redirect:/admin/stocks";
  }

  @PostMapping("/decrease/{productId}")
  public String decreaseStock(@PathVariable Integer productId,
                              @RequestParam Integer amount) {
    stockService.decreaseStock(productId, amount);
    return "redirect:/admin/stocks";
  }

  @PostMapping("/updateState/{productId}")
  public String updateStockState(@PathVariable Integer productId,
                                 @RequestParam StockState state) {
    stockService.updateState(productId, state);
    return "redirect:/admin/stocks";
  }
}

