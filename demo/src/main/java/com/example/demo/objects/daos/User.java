package com.example.demo.objects.daos;

import jakarta.persistence.*;
import com.example.demo.enums.EnumRole;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String firstName;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private EnumRole role;

    public User() {}

    public User(String id, String name, String firstName, Double amount, EnumRole role) {
        this.id        = id;
        this.name      = name;
        this.firstName = firstName;
        this.amount    = amount;
        this.role      = role;
    }

    public String getId()                  { return id; }
    public void setId(String id)           { this.id = id; }
    public String getName()                { return name; }
    public void setName(String name)       { this.name = name; }
    public String getFirstName()           { return firstName; }
    public void setFirstName(String p)     { this.firstName = p; }
    public Double getAmount()              { return amount; }
    public void setAmount(Double m)        { this.amount = m; }
    public EnumRole getRole()              { return role; }
    public void setRole(EnumRole role)     { this.role = role; }
}
