package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private double montant;
    
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private EnumRole role;
    
    public Utilisateur(){

    }

    public Utilisateur(String id, String nom, String prenom, Double montant, EnumRole role){
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.montant = montant;
        this.role = role;

    }

    public String getId(){ 
        return id;
    }
    public void setId(String id){ this.id = id;}
    
    public String getNom(){ return nom;}
    public void setNom(String nom){ this.nom = nom;}

    public String getPrenom(){ return prenom;}
    public void setPrenom(String prenom){ this.prenom = prenom;}

    public Double getMontant(){ return montant;}
    public void setMontant(Double montant){ this.montant = montant;}

    public EnumRole getRole(){ return role;}
    public void setRole(EnumRole role){ this.role = role;}
}
