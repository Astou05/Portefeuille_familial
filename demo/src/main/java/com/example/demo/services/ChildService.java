package com.example.demo.services;

// import java.util.List;
import java.util.UUID;
// import java.util.stream.collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.AppException;
import com.example.demo.enums.EnumRole;
import com.example.demo.enums.EnumType;
import com.example.demo.objects.daos.Transaction;
import com.example.demo.objects.daos.User;
import com.example.demo.objects.dtos.PagedResponse;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class ChildService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public TransactionDTO effectuerTransfert(String emetteurId,
                                            String destinataireId,
                                            Double amount) {
        if (amount == null || amount <= 0)
            throw new AppException(400, "Invalid amount. Must be > 0.");

        if (emetteurId.equals(destinataireId))
            throw new AppException(403, "Self-transfer is not allowed.");

        User emetteur = userRepository.findById(emetteurId)
            .orElseThrow(() -> new AppException(404,
                "Sender account not found: " + emetteurId));

        if (emetteur.getRole() != EnumRole.ENFANT)
            throw new AppException(403, "Only ENFANT accounts can initiate a transfer.");

        User destinataire = userRepository.findById(destinataireId)
            .orElseThrow(() -> new AppException(404,
                "Recipient account not found: " + destinataireId));

        if (destinataire.getRole() != EnumRole.ENFANT)
            throw new AppException(400, "Recipient must be a ENFANT account.");

        if (emetteur.getAmount() < amount)
            throw new AppException(403, "Insufficient funds.");

        emetteur.setAmount(emetteur.getAmount() - amount);
        destinataire.setAmount(destinataire.getAmount() + amount);
        userRepository.save(emetteur);
        userRepository.save(destinataire);

        Transaction t = new Transaction(
            "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
            amount, EnumType.TRANSFER, emetteur, destinataire
        );
        return new TransactionDTO(transactionRepository.save(t));
    }

    public User obtenirSolde(String enfantId) {
        User enfant = userRepository.findById(enfantId)
            .orElseThrow(() -> new AppException(404,
                "Account not found: " + enfantId));

        if (enfant.getRole() != EnumRole.ENFANT)
            throw new AppException(403, "This endpoint is for ENFANT accounts only.");

        return enfant;
    }

    // accepte Pageable, retourne PagedResponse<TransactionDTO>
    public PagedResponse<TransactionDTO> obtenirHistoriqueEnfant(
            String enfantId, Pageable pageable) {

        User enfant = userRepository.findById(enfantId)
            .orElseThrow(() -> new AppException(404,
                "Account not found: " + enfantId));

        if (enfant.getRole() != EnumRole.ENFANT)
            throw new AppException(403, "This endpoint is for ENFANT accounts only.");

        Page<TransactionDTO> page = transactionRepository
            .findByEmetteurIdOrDestinataireId(enfantId, enfantId, pageable)
            .map(TransactionDTO::new);

        return new PagedResponse<>(page);
    }
}