package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {


    @Id
    private String id; // String pour correspondre à ton diagramme

    private Double montant;
    private LocalDateTime date;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private com.example.demo.EnumType type;

    @ManyToOne
    @JoinColumn(name = "emetteur_id", nullable = true)
    private Utilisateur emetteur; // Lié par String automatiquement par JPA

    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;

    public Transaction() {}

    public Transaction(String id, Double montant, com.example.demo.EnumType type, Utilisateur emetteur, Utilisateur destinataire) {
        this.id = id;
        this.montant = montant;
        this.type = type;
        this.emetteur = emetteur;
        this.destinataire = destinataire;
        this.date = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public com.example.demo.EnumType getType() { return type; }
    public void setType(com.example.demo.EnumType type) { this.type = type; }
    public Utilisateur getEmetteur() { return emetteur; }
    public void setEmetteur(Utilisateur emetteur) { this.emetteur = emetteur; }
    public Utilisateur getDestinataire() { return destinataire; }
    public void setDestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }
}
