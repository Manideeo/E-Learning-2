package com.elearn.service;


import com.elearn.model.User;
import com.elearn.repo.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

 @Autowired
 private UserRepository userRepo;

 @Override
 public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     User user = userRepo.findByEmail(email)
             .orElseThrow(() -> new UsernameNotFoundException("User not found"));

return new org.springframework.security.core.userdetails.User(
             user.getEmail(),
             user.getPassword(),
             Collections.singleton(() -> user.getRole().name())
     );
 }
}
