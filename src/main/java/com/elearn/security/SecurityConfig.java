package com.elearn.security;



import com.elearn.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
@Autowired
 private JwtAuthFilter jwtAuthFilter;
@Autowired
 private CustomUserDetailsService userDetailsService;

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     return http
             .csrf(csrf -> csrf.disable())
             .authorizeHttpRequests(auth -> auth
                     .requestMatchers(
                    		
                    		 "/swagger-ui/**",
                    		 "/v3/api-docs/**",
                    		 "/swagger-ui.html",
                    		 "/api/auth/**").permitAll()
                     
                     .requestMatchers("/instructor/**").hasAuthority("INSTRUCTOR")
                     .requestMatchers("/student/**").hasAuthority("STUDENT")
                     .anyRequest().authenticated()
             )
             .sessionManagement(session -> session
                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .userDetailsService(userDetailsService)
             .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
             .build();
 }

 @Bean
 public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
 }

 @Bean
 public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
     return config.getAuthenticationManager();
 }
}
//package com.elearn.security;
//
//import com.elearn.service.CustomUserDetailsService;
//import lombok.RequiredArgsConstructor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//import org.springframework.security.authentication.*;
//import org.springframework.security.config.annotation.authentication.configuration.*;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.*;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher; // Import this
//import org.springframework.web.servlet.handler.HandlerMappingIntrospector; // Import this
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    @Autowired
//    private JwtAuthFilter jwtAuthFilter;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    // Autowire HandlerMappingIntrospector for MvcRequestMatcher.Builder
//    @Autowired
//    private HandlerMappingIntrospector handlerMappingIntrospector;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Create MvcRequestMatcher.Builder here
//        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(handlerMappingIntrospector);
//
//        return http
//                .csrf(csrf -> csrf.disable()) // Disable CSRF (common for stateless APIs)
//                .authorizeHttpRequests(auth -> auth
//                        // 1. Allow public access to authentication endpoints
//                        .requestMatchers("/api/auth/**").permitAll()
//
//                        // 2. Allow public access to Swagger UI and API documentation endpoints
//                        // These are the standard paths for springdoc-openapi
//                        .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui.html")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs/**")).permitAll()
//                        .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs")).permitAll()
//
//                        // 3. Role-based authorizations for your application paths
//                        .requestMatchers("/instructor/**").hasAuthority("INSTRUCTOR")
//                        .requestMatchers("/student/**").hasAuthority("STUDENT")
//
//                        // 4. Any other request requires authentication
//                        .anyRequest().authenticated()
//                )
//                // Configure stateless session management
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // Set custom user details service
//                .userDetailsService(userDetailsService)
//                // Add your JWT authentication filter before the Spring Security's default UsernamePasswordAuthenticationFilter
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//}