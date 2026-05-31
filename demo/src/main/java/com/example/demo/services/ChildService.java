package com.example.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.AppException;
import com.example.demo.enums.EnumRole;
import com.example.demo.enums.EnumType;
import com.example.demo.objects.daos.Transaction;
import com.example.demo.objects.daos.User;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class ChildService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 1. Transfert entre enfants
    @Transactional
    public TransactionDTO effectuerTransfert(String emetteurId, String destinataireId, Double amount) {

        if (amount == null || amount <= 0) {
            throw new AppException(400,
                "Invalid amount. The transfer amount must be strictly greater than zero. " +
                "Received value: " + amount + ".");
        }

        if (emetteurId.equals(destinataireId)) {
            throw new AppException(403,
                "Self-transfer is not allowed. The sender and beneficiary accounts must be different. " +
                "Both sender and receiver are pointing to account ID: '" + emetteurId + "'.");
        }

        User emetteur = userRepository.findById(emetteurId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. The sender account with ID '" + emetteurId +
                    "' does not exist in the system."));
                    
        if (!emetteur.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. Only E accounts are allowed to initiate a transfer. " +
                "Account '" + emetteurId + "' has role [" + emetteur.getRole() + "] and is not authorized.");
        }

        User destinataire = userRepository.findById(destinataireId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. The recipient account with ID '" + destinataireId +
                    "' does not exist in the system."));
                    
        if (!destinataire.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(400,
                "Invalid recipient. Transfers can only be made between E accounts. " +
                "Account '" + destinataireId + "' has role [" + destinataire.getRole() + "] " +
                "and cannot receive a transfer from a E.");
        }

        if (emetteur.getAmount() < amount) {
            throw new AppException(403,
                "Insufficient funds. The account balance of '" + emetteurId + "' is " +
                emetteur.getAmount() + ", which is lower than the requested transfer amount (" +
                amount + "). Please reduce the transfer amount or recharge the account first.");
        }

        emetteur.setAmount(emetteur.getAmount() - amount);
        destinataire.setAmount(destinataire.getAmount() + amount);
        userRepository.save(emetteur);
        userRepository.save(destinataire);
        
        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            amount,
            EnumType.TRANSFER,
            emetteur,
            destinataire
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    // 2. Consulter son propre solde
    public User obtenirSolde(String enfantId) {
        User enfant = userRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. No account exists with ID: " + enfantId));
        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. This endpoint is reserved for E accounts only. " +
                "Account '" + enfantId + "' has role [" + enfant.getRole() + "].");
        }
        return enfant;
    }

    // 3. Historique de l'enfant
    public List<TransactionDTO> obtenirHistoriqueEnfant(String enfantId) {
        User enfant = userRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. No account exists with ID: " + enfantId));
        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. Transaction history for this endpoint is restricted to E accounts. " +
                "Account '" + enfantId + "' has role [" + enfant.getRole() + "] and is not authorized.");
        }

        return transactionRepository
                .findByEmetteurIdOrDestinataireId(enfantId, enfantId)
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
}