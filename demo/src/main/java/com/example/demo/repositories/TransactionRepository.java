package com.example.demo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.objects.daos.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByEmetteurIdOrDestinataireId(
        String emetteurId, String destinataireId);

    Page<Transaction> findByEmetteurIdOrDestinataireId(
        String emetteurId, String destinataireId, Pageable pageable);

}