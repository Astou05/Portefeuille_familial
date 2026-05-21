package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;;

@Entity //Dire à Spring que cette class correspond à une table SQL
public class Etudiant {
    @Id //clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-incrémenté
    private long id;

    private String nom;
    private String filiere;

    public Etudiant(){}; //JPA a obligatoirement besoin d'un constructeur vide
    public Etudiant(String nom, String filiere){
        this.nom = nom;
        this.filiere = filiere;
    }

    public long getId() {
        return id;
    }
    public String getNom() { 
        return nom;
    }
    public String getFiliere() { 
        return filiere;
    }


    public void setId(long id){
        this.id = id;
    }
    public void setNom(String nom) { 
        this.nom = nom;
    }
    public void setFiliere(String filiere) { 
        this.filiere = filiere;
    }
    
}
