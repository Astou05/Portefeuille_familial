package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogErreurService {

    @Autowired
    private LogErreurRepository logErreurRepository;

    public void loguer(String endpoint, String methode, 
                       int statusCode, String message, String parametres) {
        LogErreur log = new LogErreur(endpoint, methode, statusCode, message, parametres);
        logErreurRepository.save(log);
    }
}