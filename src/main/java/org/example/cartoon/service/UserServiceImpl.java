package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Users;
import org.example.cartoon.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceInterface {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Users findByUserid(String userid) {
    return userRepository.findByUserid(userid)
        .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
  }

  @Override
  @Transactional
  public boolean updateProfile(String userid, String nickname, String currentPassword,
                               String newPassword, String confirmPassword) {

    Users user = userRepository.findByUserid(userid)
        .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

    // 1. 기존 비밀번호 확인
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
    }

    // 2. 새 비밀번호 일치 여부 확인
    if (newPassword != null && !newPassword.isBlank()) {
      if (!newPassword.equals(confirmPassword)) {
        throw new IllegalArgumentException("새 비밀번호가 서로 일치하지 않습니다.");
      }
      user.setPassword(passwordEncoder.encode(newPassword));
    }

    // 3. 닉네임 변경
    user.setNickname(nickname);
    userRepository.save(user);
    return true;
  }

  @Override
  public List<Users> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<Users> findById(Integer id) {
    return userRepository.findById(id);
  }
}
