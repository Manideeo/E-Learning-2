package com.elearn.service;

import com.elearn.model.User;
import com.elearn.model.User.Role; // Correctly imports the inner Role enum from User class
import com.elearn.repo.UserRepository;
import com.elearn.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

   
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    
    @InjectMocks
    private AuthService authService;

    private User testUser;
    private String rawPassword;
    private String encodedPassword;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Initialize common test data before each test
        rawPassword = "password123";
        encodedPassword = "encodedPassword123";
        jwtToken = "mocked.jwt.token";

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(rawPassword); 
        testUser.setRole(Role.STUDENT);
    }

    @Test
    void testRegister_Success() {
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        User savedUser = new User();
        savedUser.setEmail(testUser.getEmail());
        savedUser.setPassword(encodedPassword);
        savedUser.setRole(testUser.getRole());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(testUser.getEmail(), testUser.getRole().name())).thenReturn(jwtToken);
        String resultToken = authService.register(testUser);
        assertEquals(jwtToken, resultToken);
    }

    @Test
    void testRegister_DataIntegrityViolationException() {
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate user email"));
        assertThrows(DataIntegrityViolationException.class, () -> authService.register(testUser));
   }

    @Test
    void testLogin_Success() {
        User userFromDb = new User();
        userFromDb.setEmail(testUser.getEmail());
        userFromDb.setPassword(encodedPassword); 
        userFromDb.setRole(testUser.getRole());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(userFromDb));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(testUser.getEmail(), testUser.getRole().name())).thenReturn(jwtToken);
        String resultToken = authService.login(testUser.getEmail(), rawPassword);
        assertEquals(jwtToken, resultToken);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(testUser.getEmail(), rawPassword));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testLogin_InvalidCredentials() {
        User userFromDb = new User();
        userFromDb.setEmail(testUser.getEmail());
        userFromDb.setPassword(encodedPassword);
        userFromDb.setRole(testUser.getRole());
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(userFromDb));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(testUser.getEmail(), rawPassword));
        assertEquals("Invalid credentials", exception.getMessage());    
    }
}
