package com.example.demo;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;

    private Double montant;
    private LocalDateTime date;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EnumType type;

    @ManyToOne
    @JoinColumn(name = "emetteur_id", nullable = true)
    @JsonIgnore
    private Utilisateur emetteur;

    // ✅ Correction : nullable = true (retrait et création_argent n'ont pas de destinataire)
    @ManyToOne
    @JoinColumn(name = "destinataire_id", nullable = true)
    @JsonIgnore
    private Utilisateur destinataire;

    public Transaction() {}

    public Transaction(String id, Double montant, EnumType type,
                       Utilisateur emetteur, Utilisateur destinataire) {
        this.id          = id;
        this.montant     = montant;
        this.type        = type;
        this.emetteur    = emetteur;
        this.destinataire = destinataire;
        this.date        = LocalDateTime.now();
    }

    public String getId()                  { return id; }
    public void setId(String id)           { this.id = id; }

    public Double getMontant()             { return montant; }
    public void setMontant(Double m)       { this.montant = m; }

    public LocalDateTime getDate()         { return date; }
    public void setDate(LocalDateTime d)   { this.date = d; }

    public EnumType getType()              { return type; }
    public void setType(EnumType t)        { this.type = t; }

    public Utilisateur getEmetteur()               { return emetteur; }
    public void setEmetteur(Utilisateur e)         { this.emetteur = e; }

    public Utilisateur getDestinataire()           { return destinataire; }
    public void setDestinataire(Utilisateur d)     { this.destinataire = d; }

    // ✅ Alias utile pour compatibilité avec l'ancien code
    public Utilisateur getRecepteur()              { return this.destinataire; }
}