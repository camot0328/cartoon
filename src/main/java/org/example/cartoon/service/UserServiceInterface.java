package org.example.cartoon.service;

import org.example.cartoon.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
  Users findByUserid(String userid);
  boolean updateProfile(String userid, String nickname,
                        String currentPassword, String newPassword,
                        String confirmPassword);
  Optional<Users> findById(Integer id);
  List<Users> findAllUsers();
}
