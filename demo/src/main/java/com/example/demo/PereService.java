package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PereService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // 1. Créer de la monnaie (Alimenter le compte du Père)
    @Transactional
    public Utilisateur creerMonnaie(String pereId, Double montant) {
        Utilisateur pere = utilisateurRepository.findById(pereId)
                .orElseThrow(() -> new RuntimeException("Père non trouvé"));
        
        if (!pere.getRole().equals(EnumRole.PERE)) {
            throw new RuntimeException("Cet utilisateur n'est pas un Père");
        }

        pere.setMontant(pere.getMontant() + montant);
        utilisateurRepository.save(pere);

        // Enregistrer la transaction de création
        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.CREATION_ARGENT,
            null, // Pas d'émetteur (création ex nihilo)
            pere
        );
        transactionRepository.save(t);

        return pere;
    }

    // 2. Faire un versement à un Enfant
    @Transactional
    public Transaction faireVersement(String pereId, String enfantId, Double montant) {
        Utilisateur pere = utilisateurRepository.findById(pereId)
                .orElseThrow(() -> new RuntimeException("Père non trouvé"));
        Utilisateur enfant = utilisateurRepository.findById(enfantId)
                .orElseThrow(() -> new RuntimeException("Enfant non trouvé"));

        if (pere.getMontant() < montant) {
            throw new RuntimeException("Solde insuffisant pour effectuer ce versement");
        }

        // Mise à jour des soldes
        pere.setMontant(pere.getMontant() - montant);
        enfant.setMontant(enfant.getMontant() + montant);

        utilisateurRepository.save(pere);
        utilisateurRepository.save(enfant);

        // Enregistrer la transaction
        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            montant,
            EnumType.VERSEMENT,
            pere,
            enfant
        );
        return transactionRepository.save(t);
    }

    // 3. Voir l'historique global de la famille
    public List<Transaction> obtenirHistoriqueGlobal() {
        return transactionRepository.findAll();
    }
}
