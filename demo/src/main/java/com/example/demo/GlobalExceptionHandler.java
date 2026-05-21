package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    ex.getStatusCode());
        body.put("error",     resolveErrorLabel(ex.getStatusCode()));
        body.put("message",   ex.getMessage());

        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    // ❌ Capture les erreurs imprévues (diagramme 1 : CRASH → Erreur 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    500);
        body.put("error",     "Internal Server Error");
        body.put("message",
            "An unexpected error occurred on the server side. The transaction has been rolled back. " +
            "Please contact the administrator. Details: " + ex.getMessage());

        return ResponseEntity.status(500).body(body);
    }

    private String resolveErrorLabel(int code) {
        return switch (code) {
            case 400 -> "Bad Request";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default  -> "Error";
        };
    }
}