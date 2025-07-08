package com.elearn.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearn.model.User;
import com.elearn.repo.UserRepository;
import com.elearn.security.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;

	public String register(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		try {
			userRepo.save(user);
		} catch (DataIntegrityViolationException e) {
			throw e;

		}

		return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
	}

	public String login(String email, String password) {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}

		return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
	}
}
