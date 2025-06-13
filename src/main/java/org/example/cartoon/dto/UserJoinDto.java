package org.example.cartoon.dto;

import lombok.Data;
import org.example.cartoon.entity.Users;
import org.example.cartoon.entity.UserRole;

@Data
public class UserJoinDto {
  private String userid;
  private String nickname;
  private String password;

  public Users toEntity(String encodedPassword) {
    return Users.builder()
        .userid(this.userid)
        .nickname(this.nickname)
        .password(encodedPassword)
        .role(UserRole.USER) // 무조건 USER 고정
        .build();
  }
}
