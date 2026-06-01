package com.example.demo;

public class Wrapper<T> {
    private String message;
    private T data;          // null en cas d'echec
    private String error;     // null en cas de succes

    // Constructeur complet
    public Wrapper(String message, T data, String error) {
        this.message = message;
        this.data = data;
        this.error = error;
    }

    // Getters et Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
