package com.example.BareBoneImage.service;


import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.BareBoneImage.model.User;
import com.example.BareBoneImage.repo.UserRegistrationDto;

public interface UserService extends UserDetailsService {
   User findByEmail(String email);
   User save(UserRegistrationDto registration);
}
