package org.example.cartoon.repository;

import org.example.cartoon.entity.CartItem;
import org.example.cartoon.entity.CartItemId;
import org.example.cartoon.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {

  /**
   * userId 기준으로 장바구니 항목을 조회
   */
  List<CartItem> findByCartUserId(Integer userid);

  Optional<CartItem> findByCartUserAndProductId(Users user, Integer productId);

}
