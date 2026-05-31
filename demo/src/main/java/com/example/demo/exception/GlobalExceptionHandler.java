// package com.example.demo.exception;

// import com.example.demo.objects.dtos.Wrapper;
// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// @RestControllerAdvice
// public class GlobalExceptionHandler {

//     @ExceptionHandler(AppException.class)
//     public ResponseEntity<Wrapper<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        
//         // Logique de traitement inchangée : construction de la réponse formatée
//         // sans tenter d'injecter manuellement l'exception (géré par Spring)
//         Wrapper<Void> response = new Wrapper<>(
//             ex.getStatusCode(), 
//             resolveErrorLabel(ex.getStatusCode()), 
//             ex.getMessage()
//         );
        
//         return ResponseEntity.status(ex.getStatusCode()).body(response);
//     }

//     private String resolveErrorLabel(int code) {
//         return switch (code) {
//             case 400 -> "Bad Request";
//             case 403 -> "Forbidden";
//             case 404 -> "Not Found";
//             case 500 -> "Internal Server Error";
//             default  -> "Error";
//         };
//     }
// }

package com.example.demo.exception;

import com.example.demo.objects.dtos.Wrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Wrapper<Void>> handleAppException(AppException ex, HttpServletRequest request) {
        Wrapper<Void> response = Wrapper.failure(
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