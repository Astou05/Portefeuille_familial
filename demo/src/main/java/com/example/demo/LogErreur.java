package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_erreurs")
public class LogErreur {

    @Id
    private String id;

    private LocalDateTime date;
    private int statusCode;
    private String endpoint;
    private String methode;
    private String message;
    private String parametres;

    public LogErreur() {}

    public LogErreur(String endpoint, String methode, int statusCode, 
                     String message, String parametres) {
        this.id          = "LOG-" + java.util.UUID.randomUUID()
                               .toString().substring(0, 8).toUpperCase();
        this.date        = LocalDateTime.now();
        this.endpoint    = endpoint;
        this.methode     = methode;
        this.statusCode  = statusCode;
        this.message     = message;
        this.parametres  = parametres;
    }

    public String getId()           { return id; }
    public LocalDateTime getDate()  { return date; }
    public int getStatusCode()      { return statusCode; }
    public String getEndpoint()     { return endpoint; }
    public String getMethode()      { return methode; }
    public String getMessage()      { return message; }
    public String getParametres()   { return parametres; }
}