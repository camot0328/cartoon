package org.example.cartoon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cartoon.entity.Users;
import org.example.cartoon.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
    Users users = userRepository.findByUserid(userid)
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userid));
    log.info("유저 아이디: {}", users.getUserid()); // ✅ 안전

    return User.builder()
        .username(users.getUserid())
        .password(users.getPassword())
        .roles(users.getRole().name()) // enum에서 ROLE_USER, ROLE_ADMIN
        .build();
  }
}