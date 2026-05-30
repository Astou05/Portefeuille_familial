// package com.example.demo.services;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// import com.example.demo.models.Transaction;
// import com.example.demo.objects.dtos.TransactionDTO;
// import com.example.demo.repositories.TransactionRepository;
// import com.example.demo.repositories.UserRepository;
// import com.example.demo.models.User;
// import com.example.demo.enums.EnumRole;
// import com.example.demo.enums.EnumType;
// import com.example.demo.exception.AppException;

// @Service
// public class ParentService {

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private TransactionRepository transactionRepository;

//     // 1. Créer de la monnaie
//     @Transactional
//     public User creerMonnaie(String pereId, Double amount) {

//         if (amount == null || amount <= 0) {
//             throw new AppException(400,
//                 "Invalid amount. The amount to create must be strictly greater than zero.");
//         }

//         User pere = userRepository.findById(pereId)
//                 .orElseThrow(() -> new AppException(404,
//                     "User not found. No account exists with ID: " + pereId));
//         if (!pere.getRole().equals(EnumRole.FATHER)) {
//             throw new AppException(403,
//                 "Access denied. Only the father account is allowed to create money in the system. " +
//                 "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
//         }

//         pere.setAmount(pere.getAmount() + amount);
//         userRepository.save(pere);
        
//         Transaction t = new Transaction(
//             "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
//             amount,
//             EnumType.MONEY_CREATION,
//             null,
//             pere
//         );
//         transactionRepository.save(t);

//         return pere;
//     }

//     // 2. Verser de l'argent à un enfant
//     @Transactional
//     public TransactionDTO faireVersement(String pereId, String enfantId, Double amount) {

//         if (amount == null || amount <= 0) {
//             throw new AppException(400,
//                 "Invalid amount. The transfer amount must be strictly greater than zero.");
//         }

//         User pere = userRepository.findById(pereId)
//                 .orElseThrow(() -> new AppException(404,
//                     "Father account not found. No account exists with ID: " + pereId));
//         if (!pere.getRole().equals(EnumRole.FATHER)) {
//             throw new AppException(403,
//                 "Access denied. Only the father account is allowed to deposit money into a child's account. " +
//                 "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
//         }

//         User enfant = userRepository.findById(enfantId)
//                 .orElseThrow(() -> new AppException(404,
//                     "Child account not found. No account exists with ID: " + enfantId));
//         if (!enfant.getRole().equals(EnumRole.CHILD)) {
//             throw new AppException(400,
//                 "Invalid recipient. The account '" + enfantId + "' is not a child account. " +
//                 "The father can only deposit money into child accounts.");
//         }

//         if (pere.getAmount() < amount) {
//             throw new AppException(403,
//                 "Insufficient funds. The father's current balance (" + pere.getAmount() +
//                 ") is lower than the requested deposit amount (" + amount + "). " +
//                 "Please create money first or reduce the deposit amount.");
//         }

//         pere.setAmount(pere.getAmount() - amount);
//         enfant.setAmount(enfant.getAmount() + amount);
//         userRepository.save(pere);
//         userRepository.save(enfant);

//         Transaction t = new Transaction(
//             "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
//             amount,
//             EnumType.DEPOSIT,
//             pere,
//             enfant
//         );
//         return new TransactionDTO(transactionRepository.save(t));
//     }

//     // 3. Retirer de l'argent d'un enfant
//     @Transactional
//     public TransactionDTO retirerArgentEnfant(String pereId, String enfantId, Double amount) {

//         if (amount == null || amount <= 0) {
//             throw new AppException(400,
//                 "Invalid amount. The withdrawal amount must be strictly greater than zero.");
//         }

//         User pere = userRepository.findById(pereId)
//                 .orElseThrow(() -> new AppException(404,
//                     "Father account not found. No account exists with ID: " + pereId));
//         if (!pere.getRole().equals(EnumRole.FATHER)) {
//             throw new AppException(403,
//                 "Access denied. Only the father account is allowed to withdraw money from a child's account. " +
//                 "User '" + pereId + "' has role [" + pere.getRole() + "] and is not authorized.");
//         }

//         User enfant = userRepository.findById(enfantId)
//                 .orElseThrow(() -> new AppException(404,
//                     "Child account not found. No account exists with ID: " + enfantId));
//         if (!enfant.getRole().equals(EnumRole.CHILD)) {
//             throw new AppException(400,
//                 "Invalid source account. The account '" + enfantId + "' is not a child account. " +
//                 "The father can only withdraw money from child accounts.");
//         }

//         if (enfant.getAmount() < amount) {
//             throw new AppException(403,
//                 "Insufficient funds. The child account '" + enfantId + "' has a current balance of " +
//                 enfant.getAmount() + ", which is lower than the requested withdrawal amount (" +
//                 amount + "). The father cannot withdraw more than the child's available balance.");
//         }

//         enfant.setAmount(enfant.getAmount() - amount);
//         pere.setAmount(pere.getAmount() + amount);
//         userRepository.save(enfant);
//         userRepository.save(pere);
        
//         Transaction t = new Transaction(
//             "TX-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase(),
//             amount,
//             EnumType.WITHDRAWAL,
//             enfant,
//             pere
//         );
//         return new TransactionDTO(transactionRepository.save(t));
//     }

//     // 4. Historique global
//     public List<TransactionDTO> obtenirHistoriqueGlobal() {
//         return transactionRepository.findAll()
//                 .stream()
//                 .map(TransactionDTO::new)
//                 .collect(Collectors.toList());
//     }

//     // 5. Tous les portefeuilles
//     public List<User> obtenirTousLesPortefeuilles() {
//         return userRepository.findAll();
//     }
// }

package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.example.demo.models.Transaction;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.User;
import com.example.demo.enums.EnumRole;
import com.example.demo.enums.EnumType;
import com.example.demo.exception.AppException;

@Service
public class ParentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Utilitaire pour nettoyer les ID reçus (évite les erreurs dues aux espaces invisibles)
    private String clean(String id) {
        return id != null ? id.trim() : "";
    }

    @Transactional
    public User creerMonnaie(String pereId, Double amount) {
        if (amount == null || amount <= 0) {
            throw new AppException(400, "Invalid amount. Must be > 0.");
        }

        String id = clean(pereId);
        User pere = userRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Father account not found: " + id));

        if (pere.getRole() != EnumRole.FATHER) {
            throw new AppException(403, "Access denied. Only FATHER can create money.");
        }

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
    public TransactionDTO faireVersement(String pereId, String enfantId, Double amount) {
        if (amount == null || amount <= 0) throw new AppException(400, "Invalid amount.");

        String pid = clean(pereId);
        String eid = clean(enfantId);

        User pere = userRepository.findById(pid)
                .orElseThrow(() -> new AppException(404, "Father not found: " + pid));
        User enfant = userRepository.findById(eid)
                .orElseThrow(() -> new AppException(404, "Child not found: " + eid));

        if (pere.getRole() != EnumRole.FATHER) throw new AppException(403, "Unauthorized.");
        if (enfant.getRole() != EnumRole.CHILD) throw new AppException(400, "Recipient is not a child.");
        if (pere.getAmount() < amount) throw new AppException(403, "Insufficient funds.");

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

    @Transactional
    public TransactionDTO retirerArgentEnfant(String pereId, String enfantId, Double amount) {
        String pid = clean(pereId);
        String eid = clean(enfantId);

        User pere = userRepository.findById(pid)
                .orElseThrow(() -> new AppException(404, "Father not found: " + pid));
        User enfant = userRepository.findById(eid)
                .orElseThrow(() -> new AppException(404, "Child not found: " + eid));

        if (enfant.getAmount() < amount) throw new AppException(403, "Insufficient child funds.");

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
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }

    public List<User> obtenirTousLesPortefeuilles() {
        return userRepository.findAll();
    }
}