package com.example.demo.objects.dtos;

import com.example.demo.objects.daos.User;

public class UserDTO {

    private String id;
    private String firstName;
    private String name;
    private Double balance;
    private String role;

    public UserDTO(User user) {
        this.id        = user.getId();
        this.firstName = user.getFirstName();
        this.name      = user.getName();
        this.balance   = user.getAmount();
        this.role      = user.getRole().name();
    }

    public String getId()        { return id; }
    public String getFirstName() { return firstName; }
    public String getName()      { return name; }
    public Double getBalance()   { return balance; }
    public String getRole()      { return role; }
}