package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    // Et c'est TOUT ! Pas besoin d'écrire de code. 
    // JpaRepository contient déjà les méthodes comme save(), findAll(), findById(), deleteById().
    
}
