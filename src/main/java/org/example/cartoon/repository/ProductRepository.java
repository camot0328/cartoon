package org.example.cartoon.repository;

import org.example.cartoon.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findByTitleContaining(String keyword);
  Optional<Product> findByTitle(String title);
}
