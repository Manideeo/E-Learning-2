package com.elearn.controller;

import com.elearn.model.User;
import com.elearn.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		String token = authService.register(user); // Generate token

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "User registered successfully");
		response.put("token", token); 

		return new ResponseEntity<>(response, HttpStatus.CREATED);  
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		String token = authService.login(request.getEmail(), request.getPassword()); // Capture token

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Login successful");
		response.put("token", token); // ✅ Token added to response

		return ResponseEntity.ok(response); // 200 OK
	}

	@Data

	public static class LoginRequest {

		private String email;

		private String password;

		public String getEmail() {

			return email;

		}

		public void setEmail(String email) {

			this.email = email;

		}

		public String getPassword() {

			return password;

		}

		public void setPassword(String password) {

            this.password = password;

        }
}}