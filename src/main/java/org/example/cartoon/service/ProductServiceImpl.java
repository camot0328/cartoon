package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Product;
import org.example.cartoon.entity.Stock;
import org.example.cartoon.entity.StockState;
import org.example.cartoon.repository.ProductRepository;
import org.example.cartoon.repository.StockRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductServiceInterface {

  private final ProductRepository productRepository;
  private final StockRepository stockRepository;

  @Override
  public List<org.example.cartoon.entity.Product> getAllProducts() {
    List<Product> result = productRepository.findAll();
    log.info("전체 상품 조회: {}", result); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return result;
  }

  @Override
  public Product getProductById(Integer id) {
    Product product = productRepository.findById(id)
                      .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));
    log.info("단일 상품 조회 (id={}): {}", id, product); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return product;
  }

  @Override
  public List<Product> searchProducts(String keyword) {
    List<Product> result = productRepository.findByTitleContaining(keyword);
    log.info("검색된 상품(keyword={}): {}", keyword, result); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return result;
  }

  @Override
  public Product saveProduct(Product product) {
    Product saved = productRepository.save(product);
    log.info("상품 저장됨: {}", saved); // 👉 [디버깅용] 나중에 제거 또는 주석 처리하세요.
    return saved;
  }

  @Override
  public void updateProduct(Product product) {
    productRepository.save(product); // JPA의 save는 insert + update 처리됨
  }

  @Override
  public List<Product> getVisibleProducts() {
    List<Stock> stocks = stockRepository.findByStateNot(StockState.삭제됨);
    return stocks.stream()
        .map(Stock::getProduct)
        .collect(Collectors.toList());
  }

  @Override
  public Product findByTitle(String title) {
    return productRepository.findByTitle(title)
        .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));
  }

  @Override
  public List<Product> getAllForAdmin() {
    return productRepository.findAll(); // 삭제 포함 전체
  }

  @Override
  @Transactional
  public void softDeleteProduct(Integer productId) {
    Product product = productRepository.findByIdWithStock(productId);
    Stock stock = stockRepository.findByProduct(product)
        .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보가 없습니다."));
    stock.setState(StockState.삭제됨);
    stockRepository.save(stock);
  }

  @Override
  @Transactional
  public void restoreProduct(Integer productId) {
    Product product = productRepository.findByIdWithStock(productId);
    Stock stock = stockRepository.findByProduct(product)
        .orElseThrow(() -> new IllegalArgumentException("해당 상품의 재고 정보가 없습니다."));
    stock.setState(StockState.판매중);
    stockRepository.save(stock);
  }

  @Override
  public List<Product> searchByKeywordWithStock(String keyword) {
    return productRepository.findByTitleContainingOrAuthorContainingWithStock(keyword, keyword);
  }

  @Override
  public Page<Product> searchByKeywordWithStockPaged(String keyword, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return productRepository.findByKeywordWithStock(keyword, pageable);
  }

  @Override
  public List<Product> getLatestProducts() {
    return productRepository.findTop10ByOrderByIdDesc(); // 또는 최신 등록일 기준
  }

  @Override
  public Page<Product> getAllWithStock(Pageable pageable) {
    return productRepository.findAllWithStock(pageable);
  }

}
