// package com.example.demo.controllers;

// import com.example.demo.models.User;
// import com.example.demo.objects.dtos.TransactionDTO;
// import com.example.demo.objects.dtos.Wrapper;
// import com.example.demo.services.ParentService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/v1/fathers")
// public class ParentController {

//     @Autowired
//     private ParentService parentService;

//     // 1. POST /api/v1/fathers/issue-money
//     @PostMapping("/issue-money")
//     public ResponseEntity<Wrapper<User>> issueMoney(@RequestBody Map<String, Object> payload) {
//         String fatherId = (String) payload.get("fatherId");
//         Double amount   = Double.parseDouble(payload.get("amount").toString());
//         User updatedFather = parentService.creerMonnaie(fatherId, amount);
//         return ResponseEntity.status(201).body(
//             new Wrapper<>(201, "Money successfully issued.", updatedFather)
//         );
//     }

//     // 2. POST /api/v1/fathers/deposit
//     @PostMapping("/deposit")
//     public ResponseEntity<Wrapper<TransactionDTO>> makeDeposit(@RequestBody Map<String, Object> payload) {
//         String fatherId = (String) payload.get("fatherId");
//         String childId  = (String) payload.get("childId");
//         Double amount   = Double.parseDouble(payload.get("amount").toString());
//         TransactionDTO tx = parentService.faireVersement(fatherId, childId, amount);
//         return ResponseEntity.status(201).body(
//             new Wrapper<>(201, "Deposit successfully executed.", tx)
//         );
//     }

//     // 3. POST /api/v1/fathers/withdraw
//     @PostMapping("/withdraw")
//     public ResponseEntity<Wrapper<TransactionDTO>> withdrawFromChild(@RequestBody Map<String, Object> payload) {
//         String fatherId = (String) payload.get("fatherId");
//         String childId  = (String) payload.get("childId");
//         Double amount   = Double.parseDouble(payload.get("amount").toString());
//         TransactionDTO tx = parentService.retirerArgentEnfant(fatherId, childId, amount);
//         return ResponseEntity.status(201).body(
//             new Wrapper<>(201, "Withdrawal successfully executed.", tx)
//         );
//     }

//     // 4. GET /api/v1/fathers/history
//     @GetMapping("/history")
//     public ResponseEntity<Wrapper<List<TransactionDTO>>> getHistory() {
//         List<TransactionDTO> history = parentService.obtenirHistoriqueGlobal();
//         return ResponseEntity.ok(
//             new Wrapper<>(200, "Transaction history retrieved successfully.", history)
//         );
//     }

//     // 5. GET /api/v1/fathers/wallets
//     @GetMapping("/wallets")
//     public ResponseEntity<Wrapper<List<User>>> getAllWallets() {
//         List<User> wallets = parentService.obtenirTousLesPortefeuilles();
//         return ResponseEntity.ok(
//             new Wrapper<>(200, "Wallets list retrieved successfully.", wallets)
//         );
//     }
// }


package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ParentService;
import com.example.demo.exception.AppException; // Assure-toi d'importer ton exception
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fathers")
public class ParentController {

    @Autowired
    private ParentService parentService;

    // Méthode utilitaire pour extraire et valider les paramètres
    private String getRequiredParam(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key) || payload.get(key) == null) {
            throw new AppException(400, "Missing required parameter: " + key);
        }
        return payload.get(key).toString();
    }

    // 1. POST /api/v1/fathers/issue-money
    @PostMapping("/issue-money")
    public ResponseEntity<Wrapper<User>> issueMoney(@RequestBody Map<String, Object> payload) {
        String fatherId = getRequiredParam(payload, "fatherId");
        Double amount = Double.parseDouble(getRequiredParam(payload, "amount"));
        
        User updatedFather = parentService.creerMonnaie(fatherId, amount);
        return ResponseEntity.status(201).body(
            new Wrapper<>(201, "Money successfully issued.", updatedFather)
        );
    }

    // 2. POST /api/v1/fathers/deposit
    @PostMapping("/deposit")
    public ResponseEntity<Wrapper<TransactionDTO>> makeDeposit(@RequestBody Map<String, Object> payload) {
        String fatherId = getRequiredParam(payload, "fatherId");
        String childId = getRequiredParam(payload, "childId");
        Double amount = Double.parseDouble(getRequiredParam(payload, "amount"));
        
        TransactionDTO tx = parentService.faireVersement(fatherId, childId, amount);
        return ResponseEntity.status(201).body(
            new Wrapper<>(201, "Deposit successfully executed.", tx)
        );
    }

    // 3. POST /api/v1/fathers/withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<Wrapper<TransactionDTO>> withdrawFromChild(@RequestBody Map<String, Object> payload) {
        String fatherId = getRequiredParam(payload, "fatherId");
        String childId = getRequiredParam(payload, "childId");
        Double amount = Double.parseDouble(getRequiredParam(payload, "amount"));
        
        TransactionDTO tx = parentService.retirerArgentEnfant(fatherId, childId, amount);
        return ResponseEntity.status(201).body(
            new Wrapper<>(201, "Withdrawal successfully executed.", tx)
        );
    }

    // 4. GET /api/v1/fathers/history
    @GetMapping("/history")
    public ResponseEntity<Wrapper<List<TransactionDTO>>> getHistory() {
        return ResponseEntity.ok(
            new Wrapper<>(200, "Transaction history retrieved successfully.", parentService.obtenirHistoriqueGlobal())
        );
    }

    // 5. GET /api/v1/fathers/wallets
    @GetMapping("/wallets")
    public ResponseEntity<Wrapper<List<User>>> getAllWallets() {
        return ResponseEntity.ok(
            new Wrapper<>(200, "Wallets list retrieved successfully.", parentService.obtenirTousLesPortefeuilles())
        );
    }
}