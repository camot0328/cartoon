package org.example.cartoon.service;

import org.example.cartoon.entity.Product;
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

}
