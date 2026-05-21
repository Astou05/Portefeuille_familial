package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnfantService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 1. Transfert entre enfants
    @Transactional
    public TransactionDTO effectuerTransfert(String emetteurId, String destinataireId, Double montant) {

        if (montant == null || montant <= 0) {
            throw new AppException(400,
                "Invalid amount. The transfer amount must be strictly greater than zero. " +
                "Received value: " + montant + ".");
        }

        if (emetteurId.equals(destinataireId)) {
            throw new AppException(403,
                "Self-transfer is not allowed. The sender and beneficiary accounts must be different. " +
                "Both sender and receiver are pointing to account ID: '" + emetteurId + "'.");
        }

        Utilisateur emetteur = utilisateurRepository.findById(emetteurId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. The sender account with ID '" + emetteurId +
                    "' does not exist in the system."));

        if (!emetteur.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. Only child accounts are allowed to initiate a transfer. " +
                "Account '" + emetteurId + "' has role [" + emetteur.getRole() + "] and is not authorized.");
        }

        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. The recipient account with ID '" + destinataireId +
                    "' does not exist in the system."));

        if (!destinataire.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(400,
                "Invalid recipient. Transfers can only be made between child accounts. " +
                "Account '" + destinataireId + "' has role [" + destinataire.getRole() + "] " +
                "and cannot receive a transfer from a child.");
        }

        if (emetteur.getMontant() < montant) {
            throw new AppException(403,
                "Insufficient funds. The account balance of '" + emetteurId + "' is " +
                emetteur.getMontant() + ", which is lower than the requested transfer amount (" +
                montant + "). Please reduce the transfer amount or recharge the account first.");
        }

        emetteur.setMontant(emetteur.getMontant() - montant);
        destinataire.setMontant(destinataire.getMontant() + montant);
        utilisateurRepository.save(emetteur);
        utilisateurRepository.save(destinataire);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.TRANSFERT,
            emetteur,
            destinataire
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    // 2. Consulter son propre solde
    public Utilisateur obtenirSolde(String enfantId) {
        Utilisateur enfant = utilisateurRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. No account exists with ID: " + enfantId));

        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. This endpoint is reserved for child accounts only. " +
                "Account '" + enfantId + "' has role [" + enfant.getRole() + "].");
        }
        return enfant;
    }

    // 3. Historique de l'enfant
    public List<TransactionDTO> obtenirHistoriqueEnfant(String enfantId) {
        Utilisateur enfant = utilisateurRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Account not found. No account exists with ID: " + enfantId));

        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(403,
                "Access denied. Transaction history for this endpoint is restricted to child accounts. " +
                "Account '" + enfantId + "' has role [" + enfant.getRole() + "] and is not authorized.");
        }

        return transactionRepository
                .findByEmetteurIdOrDestinataireId(enfantId, enfantId)
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
}