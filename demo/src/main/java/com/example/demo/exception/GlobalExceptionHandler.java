package com.example.demo.exception;

import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ErrorLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogService errorLogService; // Correction de la casse (CamelCase académique)

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Wrapper<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        
        // COMMENTÉ OU SUPPRIMÉ : L'appel à errorLogService.loguer(...) est retiré ici.
        // C'est désormais le LogInterceptor qui capture automatiquement cette exception
        // dans sa méthode afterCompletion et qui l'enregistre de manière centralisée.
        
        // Nous construisons uniquement la structure de la réponse d'erreur HTTP pour le client
        Wrapper<Void> response = new Wrapper<>(
            ex.getStatusCode(), 
            resolveErrorLabel(ex.getStatusCode()), 
            ex.getMessage()
        );
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
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