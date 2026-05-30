package com.example.demo.objects.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) // Masque les champs nulls dans le JSON final
public class Wrapper<T> {
    private String timestamp;
    private int status;
    private String message;
    private T data;
    private String error;

    // Constructeur Succès
    public Wrapper(int status, String message, T data) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    // Constructeur Échec
    public Wrapper(int status, String error, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.data = null;
    }

    // Getters et Setters
    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getError() { return error; }
}

