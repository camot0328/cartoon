package org.example.cartoon.repository;

import org.example.cartoon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
  List<Product> findByTitleContaining(String keyword);
  Optional<Product> findByTitle(String title);

  @Query("SELECT p FROM Product p JOIN FETCH p.stock s WHERE p.id = :id")
  Product findByIdWithStock(@Param("id") Integer id);

  @Query("SELECT p FROM Product p JOIN FETCH p.stock s " +
      "WHERE s.state != '삭제됨' AND (p.title LIKE %:keyword% OR p.author LIKE %:keyword%)")
  List<Product> findByTitleContainingOrAuthorContainingWithStock(@Param("keyword") String title,
                                                                 @Param("keyword") String author);

  @Query(value = "SELECT p FROM Product p JOIN p.stock s " +
      "WHERE p.title LIKE %:keyword% OR p.author LIKE %:keyword% OR p.publisher LIKE %:keyword%",
      countQuery = "SELECT COUNT(p) FROM Product p " +
          "WHERE p.title LIKE %:keyword% OR p.author LIKE %:keyword% OR p.publisher LIKE %:keyword%")
  Page<Product> findByKeywordWithStock(@Param("keyword") String keyword, Pageable pageable);

  List<Product> findTop10ByOrderByIdDesc();

  @Query(value = "SELECT p FROM Product p",
      countQuery = "SELECT count(p) FROM Product p")
  Page<Product> findAllWithStock(Pageable pageable);
}
