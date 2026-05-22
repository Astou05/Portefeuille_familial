package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private LogErreurService logErreurService;

    // ✅ Erreurs métier (400, 403, 404)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(
            AppException ex, HttpServletRequest request) {

        // 📝 Journaliser l'erreur en base
        logErreurService.loguer(
            request.getRequestURI(),
            request.getMethod(),
            ex.getStatusCode(),
            ex.getMessage(),
            "Query: " + (request.getQueryString() != null ? request.getQueryString() : "none")
        );

        return buildResponse(ex.getStatusCode(), resolveErrorLabel(ex.getStatusCode()), ex.getMessage());
    }

    // ✅ Mauvaise méthode HTTP → 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        String message = "HTTP method '" + ex.getMethod() + "' is not supported for this endpoint. " +
                         "Supported method(s): " + ex.getSupportedHttpMethods() + ".";

        logErreurService.loguer(
            request.getRequestURI(),
            request.getMethod(),
            405,
            message,
            "none"
        );

        return buildResponse(405, "Method Not Allowed", message);
    }

    // ✅ Body JSON absent ou malformé → 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableBody(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String message = "The request body is missing or contains invalid JSON. " +
                         "Please provide a valid JSON body with all required fields.";

        logErreurService.loguer(
            request.getRequestURI(),
            request.getMethod(),
            400,
            message,
            "none"
        );

        return buildResponse(400, "Bad Request", message);
    }

    // ✅ Route introuvable → 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandler(
            NoHandlerFoundException ex, HttpServletRequest request) {

        String message = "The requested endpoint '" + ex.getRequestURL() + "' does not exist. " +
                         "Please check the URL and try again.";

        logErreurService.loguer(
            request.getRequestURI(),
            request.getMethod(),
            404,
            message,
            "none"
        );

        return buildResponse(404, "Not Found", message);
    }

    // ✅ Crash inattendu → 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {

        String message = "An unexpected error occurred on the server side. " +
                         "The transaction has been rolled back. " +
                         "Please contact the administrator. Details: " + ex.getMessage();

        logErreurService.loguer(
            request.getRequestURI(),
            request.getMethod(),
            500,
            message,
            "none"
        );

        return buildResponse(500, "Internal Server Error", message);
    }

    // ✅ Méthode utilitaire
    private ResponseEntity<Map<String, Object>> buildResponse(
            int status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    status);
        body.put("error",     error);
        body.put("message",   message);
        return ResponseEntity.status(status).body(body);
    }

    private String resolveErrorLabel(int code) {
        return switch (code) {
            case 400 -> "Bad Request";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            default  -> "Error";
        };
    }
}