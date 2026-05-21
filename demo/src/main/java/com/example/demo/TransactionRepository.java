package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // L'import manquant qui cause l'erreur !

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByEmetteurIdOrDestinataireId(String emetteurId, String destinataireId);
}
