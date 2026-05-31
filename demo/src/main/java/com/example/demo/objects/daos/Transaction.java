package com.example.demo.objects.daos;

import jakarta.persistence.*;
import com.example.demo.enums.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    private Double amount;
    private LocalDateTime date;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EnumType type;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = true)   // ← corrigé
    @JsonIgnore
    private User emetteur;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = true) // ← corrigé
    @JsonIgnore
    private User destinataire;

    public Transaction() {}

    public Transaction(String id, Double amount, EnumType type,
                       User emetteur, User destinataire) {
        this.id           = id;
        this.amount       = amount;
        this.type         = type;
        this.emetteur     = emetteur;
        this.destinataire = destinataire;
        this.date         = LocalDateTime.now();
    }

    public String getId()              { return id; }
    public void setId(String id)       { this.id = id; }
    public Double getAmount()          { return amount; }
    public void setAmount(Double m)    { this.amount = m; }
    public LocalDateTime getDate()     { return date; }
    public void setDate(LocalDateTime d){ this.date = d; }
    public EnumType getType()          { return type; }
    public void setType(EnumType t)    { this.type = t; }
    public User getEmetteur()          { return emetteur; }
    public void setEmetteur(User e)    { this.emetteur = e; }
    public User getDestinataire()      { return destinataire; }
    public void setDestinataire(User d){ this.destinataire = d; }
    public User getRecepteur()         { return this.destinataire; }
}