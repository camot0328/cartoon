package org.example.cartoon.repository;

import org.example.cartoon.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
  Optional<Users> findByUserid(String userid);
}
