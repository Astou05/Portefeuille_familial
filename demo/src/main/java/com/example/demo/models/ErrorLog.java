package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_logs") // Un nom plus général car il contient aussi les succès
public class ErrorLog {

    @Id
    private String id;
    private LocalDateTime date;
    private int statusCode;
    private String endpoint;
    private String method;
    private String message;
    private String parameters;

    public ErrorLog() {}

    public ErrorLog(String endpoint, String method, int statusCode, String message, String parameters) {
        this.id = "LOG-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.date = LocalDateTime.now();
        this.endpoint = endpoint;
        this.method = method;
        this.statusCode = statusCode;
        this.message = message;
        this.parameters = parameters;
    }

    // Getters et Setters
    public String getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public int getStatusCode() { return statusCode; }
    public String getEndpoint() { return endpoint; }
    public String getMethode() { return method; }
    public String getMessage() { return message; }
    public String getParametres() { return parameters; }
}