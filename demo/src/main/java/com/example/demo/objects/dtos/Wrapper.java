// package com.example.demo.objects.dtos;

// import com.fasterxml.jackson.annotation.JsonInclude;
// import java.time.LocalDateTime;

// @JsonInclude(JsonInclude.Include.NON_NULL) // Masque les champs nulls dans le JSON final
// public class Wrapper<T> {
//     private String timestamp;
//     private int status;
//     private String message;
//     private T data;
//     private String error;

//     // Constructeur Succès
//     public Wrapper(int status, String message, T data) {
//         this.timestamp = LocalDateTime.now().toString();
//         this.status = status;
//         this.message = message;
//         this.data = data;
//         this.error = null;
//     }

//     // Constructeur Échec
//     public Wrapper(int status, String error, String message) {
//         this.timestamp = LocalDateTime.now().toString();
//         this.status = status;
//         this.error = error;
//         this.message = message;
//         this.data = null;
//     }

//     // Getters et Setters
//     public String getTimestamp() { return timestamp; }
//     public int getStatus() { return status; }
//     public String getMessage() { return message; }
//     public T getData() { return data; }
//     public String getError() { return error; }
// }


package com.example.demo.objects.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wrapper<T> {

    private String timestamp;
    private int status;
    private String message;
    private T data;
    private String error;

    private Wrapper() {
        this.timestamp = LocalDateTime.now().toString();
    }

    // Remplace les 2 constructeurs ambigus par 2 méthodes statiques claires
    public static <T> Wrapper<T> success(int status, String message, T data) {
        Wrapper<T> w = new Wrapper<>();
        w.status  = status;
        w.message = message;
        w.data    = data;
        return w;
    }

    public static <T> Wrapper<T> failure(int status, String error, String message) {
        Wrapper<T> w = new Wrapper<>();
        w.status  = status;
        w.error   = error;
        w.message = message;
        return w;
    }

    public String getTimestamp() { return timestamp; }
    public int getStatus()       { return status; }
    public String getMessage()   { return message; }
    public T getData()           { return data; }
    public String getError()     { return error; }
}