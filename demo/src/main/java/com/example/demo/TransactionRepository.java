package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    // ✅ Utilisé pour l'historique d'un enfant (émetteur OU destinataire)
    List<Transaction> findByEmetteurIdOrDestinataireId(String emetteurId, String destinataireId);
}