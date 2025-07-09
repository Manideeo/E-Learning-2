package com.elearn.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDuplicateEmail(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Email already exists");
        response.put("message", ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Constraint violation");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
    }

   
    @ExceptionHandler(InstructorNotFoundException.class)
    public ResponseEntity<?> handleInstructorNotFound(InstructorNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Instructor Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); 
    }

  
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Access Denied");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); 
    }

   
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
    }

  
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); 
    }
}
