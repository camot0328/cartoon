package org.example.cartoon.service;

import org.example.cartoon.entity.CartItem;
import java.util.List;

public interface CartServiceInterface {
  /**
   * 유저 ID 기준으로 장바구니 항목들을 조회한다.
   * @param userid - 사용자 ID
   * @return 장바구니 항목 리스트
   */
  List<CartItem> getCartItems(String userid);

  void updateQuantity(String userid, Integer productId, Integer quantity);

  void removeItem(String userid, Integer productId);
}
