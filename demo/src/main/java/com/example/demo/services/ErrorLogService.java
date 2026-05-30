package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.ErrorLog;
import com.example.demo.repositories.ErrorLogRepository;

@Service
public class ErrorLogService {

    @Autowired
    private ErrorLogRepository ErrorLogRepository;

    public void loguer(String endpoint, String methode, 
                    int statusCode, String message, String parametres) {
        ErrorLog log = new ErrorLog(endpoint, methode, statusCode, message, parametres);
        ErrorLogRepository.save(log);
    }
}

