package org.example.cartoon.service;

import org.example.cartoon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductServiceInterface {
  List<Product> getAllProducts();
  Product getProductById(Integer id);
  Product findByTitle(String title);
  List<Product> searchProducts(String keyword);
  Product saveProduct(Product product);
  void updateProduct(Product product);
  List<Product> getAllForAdmin();
  void softDeleteProduct(Integer productId);
  void restoreProduct(Integer productId);
  List<Product> getVisibleProducts();
  List<Product> searchByKeywordWithStock(String keyword);
  Page<Product> searchByKeywordWithStockPaged(String keyword, int page, int size);
  List<Product> getLatestProducts();
  Page<Product> getAllWithStock(Pageable pageable);

}
