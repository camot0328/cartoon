package org.example.cartoon.repository;

import org.example.cartoon.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
  Optional<Cart> findByUsersUserid(Integer userid); // 필요 시 사용
}
