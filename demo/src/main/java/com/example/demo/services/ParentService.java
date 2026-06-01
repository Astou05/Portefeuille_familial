package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.demo.objects.daos.Transaction;
import com.example.demo.objects.daos.User;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.enums.EnumRole;
import com.example.demo.enums.EnumType;
import com.example.demo.exception.AppException;

@Service
public class ParentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    
    private String clean(String id) {
        return id != null ? id.trim() : "";
    }

    private User getPERE() {
        return userRepository.findByRole(EnumRole.PERE)
            .orElseThrow(() -> new AppException(404, "PERE account not found."));
    }

    @Transactional
    public User creerMonnaie(Double amount) {
        if (amount == null || amount <= 0) {
            throw new AppException(400, "Invalid amount. Must be > 0.");
        }

        User pere = getPERE(); // ← automatique

        pere.setAmount(pere.getAmount() + amount);
        userRepository.save(pere);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            amount, EnumType.MONEY_CREATION, null, pere
        );
        transactionRepository.save(t);
        return pere;
    }

    
    @Transactional
    public TransactionDTO faireVersement(String enfantId, Double amount) {
        if (amount == null || amount <= 0)
            throw new AppException(400, "Invalid amount.");

        String eid = clean(enfantId); 

        User pere   = getPERE(); 
        User enfant = userRepository.findById(eid)
                .orElseThrow(() -> new AppException(404, "ENFANT not found: " + eid));

        if (enfant.getRole() != EnumRole.ENFANT)
            throw new AppException(400, "Recipient is not a ENFANT.");
        if (pere.getAmount() < amount)
            throw new AppException(403, "Insufficient funds.");

        pere.setAmount(pere.getAmount() - amount);
        enfant.setAmount(enfant.getAmount() + amount);
        userRepository.save(pere);
        userRepository.save(enfant);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            amount, EnumType.DEPOSIT, pere, enfant
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    // ← AVANT : retirerArgentEnfant(String pereId, String enfantId, Double amount)
    // ← APRÈS : plus de pereId
    @Transactional
    public TransactionDTO retirerArgentEnfant(String enfantId, Double amount) {
        if (amount == null || amount <= 0)
            throw new AppException(400, "Invalid amount.");

        String eid = clean(enfantId); 

        User pere   = getPERE(); 
        User enfant = userRepository.findById(eid)
                .orElseThrow(() -> new AppException(404, "ENFANT not found: " + eid));

        if (enfant.getRole() != EnumRole.ENFANT)
            throw new AppException(400, "Source account is not a ENFANT.");
        if (enfant.getAmount() < amount)
            throw new AppException(403, "Insufficient ENFANT funds.");

        enfant.setAmount(enfant.getAmount() - amount);
        pere.setAmount(pere.getAmount() + amount);
        userRepository.save(enfant);
        userRepository.save(pere);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            amount, EnumType.WITHDRAWAL, enfant, pere
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    public List<TransactionDTO> obtenirHistoriqueGlobal() {
        return transactionRepository.findAll()
            .stream()
            .map(TransactionDTO::new)
            .collect(Collectors.toList());
    }

    public List<User> obtenirTousLesPortefeuilles() {
        return userRepository.findAll();
    }
}