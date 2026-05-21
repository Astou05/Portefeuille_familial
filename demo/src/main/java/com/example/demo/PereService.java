package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PereService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 1. Créer de la monnaie
    @Transactional
    public Utilisateur creerMonnaie(String pereId, Double montant) {

        if (montant == null || montant <= 0) {
            throw new AppException(400,
                "Invalid amount. The amount to create must be strictly greater than zero.");
        }

        Utilisateur pere = utilisateurRepository.findById(pereId)
                .orElseThrow(() -> new AppException(404,
                    "User not found. No account exists with ID: " + pereId));

        if (!pere.getRole().equals(EnumRole.PERE)) {
            throw new AppException(403,
                "Access denied. Only the father account is allowed to create money in the system. " +
                "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
        }

        pere.setMontant(pere.getMontant() + montant);
        utilisateurRepository.save(pere);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.CREATION_ARGENT,
            null,
            pere
        );
        transactionRepository.save(t);

        return pere;
    }

    // 2. Verser de l'argent à un enfant
    @Transactional
    public TransactionDTO faireVersement(String pereId, String enfantId, Double montant) {

        if (montant == null || montant <= 0) {
            throw new AppException(400,
                "Invalid amount. The transfer amount must be strictly greater than zero.");
        }

        Utilisateur pere = utilisateurRepository.findById(pereId)
                .orElseThrow(() -> new AppException(404,
                    "Father account not found. No account exists with ID: " + pereId));

        if (!pere.getRole().equals(EnumRole.PERE)) {
            throw new AppException(403,
                "Access denied. Only the father account is allowed to deposit money into a child's account. " +
                "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
        }

        Utilisateur enfant = utilisateurRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Child account not found. No account exists with ID: " + enfantId));

        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(400,
                "Invalid recipient. The account '" + enfantId + "' is not a child account. " +
                "The father can only deposit money into child accounts.");
        }

        if (pere.getMontant() < montant) {
            throw new AppException(403,
                "Insufficient funds. The father's current balance (" + pere.getMontant() +
                ") is lower than the requested deposit amount (" + montant + "). " +
                "Please create money first or reduce the deposit amount.");
        }

        pere.setMontant(pere.getMontant() - montant);
        enfant.setMontant(enfant.getMontant() + montant);
        utilisateurRepository.save(pere);
        utilisateurRepository.save(enfant);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.VERSEMENT,
            pere,
            enfant
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    // 3. Retirer de l'argent d'un enfant
    @Transactional
    public TransactionDTO retirerArgentEnfant(String pereId, String enfantId, Double montant) {

        if (montant == null || montant <= 0) {
            throw new AppException(400,
                "Invalid amount. The withdrawal amount must be strictly greater than zero.");
        }

        Utilisateur pere = utilisateurRepository.findById(pereId)
                .orElseThrow(() -> new AppException(404,
                    "Father account not found. No account exists with ID: " + pereId));

        if (!pere.getRole().equals(EnumRole.PERE)) {
            throw new AppException(403,
                "Access denied. Only the father account is allowed to withdraw money from a child's account. " +
                "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
        }

        Utilisateur enfant = utilisateurRepository.findById(enfantId)
                .orElseThrow(() -> new AppException(404,
                    "Child account not found. No account exists with ID: " + enfantId));

        if (!enfant.getRole().equals(EnumRole.ENFANT)) {
            throw new AppException(400,
                "Invalid source account. The account '" + enfantId + "' is not a child account. " +
                "The father can only withdraw money from child accounts.");
        }

        if (enfant.getMontant() < montant) {
            throw new AppException(403,
                "Insufficient funds. The child account '" + enfantId + "' has a current balance of " +
                enfant.getMontant() + ", which is lower than the requested withdrawal amount (" +
                montant + "). The father cannot withdraw more than the child's available balance.");
        }

        enfant.setMontant(enfant.getMontant() - montant);
        pere.setMontant(pere.getMontant() + montant);
        utilisateurRepository.save(enfant);
        utilisateurRepository.save(pere);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.RETRAIT,
            enfant,
            pere
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    // 4. Historique global
    public List<TransactionDTO> obtenirHistoriqueGlobal() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

    // 5. Tous les portefeuilles
    public List<Utilisateur> obtenirTousLesPortefeuilles() {
        return utilisateurRepository.findAll();
    }
}