package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.CartItem;
import org.example.cartoon.entity.Users;
import org.example.cartoon.repository.CartItemRepository;
import org.example.cartoon.repository.CartRepository;
import org.example.cartoon.repository.ProductRepository;
import org.example.cartoon.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartServiceInterface {

  private final CartItemRepository cartItemRepository;
  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final ProductRepository productRepository;

  @Override
  public List<CartItem> getCartItems(Integer userid) {
    Users user = userRepository.findById(userid)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userid));

    log.info("CartService: {}의 장바구니 항목 조회", userid);

    return cartItemRepository.findByCartUserId(user.getId());
  }

  @Override
  public void updateQuantity(Integer userId, Integer productId, Integer quantity) {
    Users user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

    CartItem item = cartItemRepository.findByCartUserAndProductId(user, productId)
        .orElseThrow(() -> new IllegalArgumentException("장바구니 항목 없음"));
    item.setQuantity(quantity);
    cartItemRepository.save(item);
  }

  @Override
  public void removeItem(Integer userId, Integer productId) {
    Users user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

    CartItem item = cartItemRepository.findByCartUserAndProductId(user, productId)
        .orElseThrow(() -> new IllegalArgumentException("장바구니 항목을 찾을 수 없습니다."));

    cartItemRepository.delete(item);
  }

}
