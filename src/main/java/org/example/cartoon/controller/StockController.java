package org.example.cartoon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.service.StockServiceInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/stock")
public class StockController {

  private final StockServiceInterface stockService;

  @GetMapping("/list")
  public String list(Model model) {
    List<Stock> stocks = stockService.getAllStock();
    model.addAttribute("stocks", stocks);
    log.info("재고 리스트 요청"); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return "stock/stockList";
  }

  @GetMapping("/add")
  public String addForm(@RequestParam Integer productId, Model model) {
    Stock stock = stockService.getStockByProductId(productId);
    model.addAttribute("stock", stock);
    return "stock/stockAdd"; // 수량 입력 form
  }

  @PostMapping("/add")
  public String addStock(@ModelAttribute Stock stock) {
    stockService.saveStock(stock);
    log.info("재고 추가 또는 수정 요청: {}", stock); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return "redirect:/stock/list";
  }
}
