package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.models.ErrorLog;
import com.example.demo.repositories.ErrorLogRepository;

@Component
public class LogInterceptor implements HandlerInterceptor {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        int status = response.getStatus();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString() != null ? request.getQueryString() : "";

        // Déterminer le message contextuel
        String message = "Request processed with status " + status;
        if (ex != null) {
            message = ex.getMessage();
        } else if (status >= 200 && status < 300) {
            message = "Operation executed successfully.";
        }

        // On ignore les requêtes de l'UI de la base de données (comme H2 console) si nécessaire
        if (!uri.contains("/h2-console") && !uri.contains("/favicon.ico")) {
            ErrorLog log = new ErrorLog(uri, method, status, message, queryString);
            errorLogRepository.save(log);
        }
    }
}