package com.example.BareBoneImage.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BareBoneImage.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByEmail(String email);
}


