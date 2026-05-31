package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.objects.daos.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    // ✅ Utilisé pour l'historique d'un enfant (émetteur OU destinataire)
    List<Transaction> findByEmetteurIdOrDestinataireId(String emetteurId, String destinataireId);
}

