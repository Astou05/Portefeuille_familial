package com.example.demo.controllers;

import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.objects.dtos.UserDTO;
import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ParentService;
import com.example.demo.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/father") 
public class ParentController {

    @Autowired
    private ParentService parentService;

    private String getParam(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key) || payload.get(key) == null) {
            throw new AppException(400, "Missing required parameter: " + key);
        }
        return payload.get(key).toString();
    }

    private Double getAmount(Map<String, Object> payload) {
        return Double.parseDouble(getParam(payload, "amount"));
    }

    // POST /api/v1/father/balance
    @PostMapping("/balance")
    public ResponseEntity<Wrapper<UserDTO>> issueMoney(
            @RequestBody Map<String, Object> payload) {

        Double amount = getAmount(payload);
        UserDTO dto = new UserDTO(parentService.creerMonnaie(amount)); // ← plus d'id
        return ResponseEntity.status(201).body(
            Wrapper.success(201, "Money successfully issued.", dto)
        );
    }

    // POST /api/v1/father/deposits
    @PostMapping("/deposits")
    public ResponseEntity<Wrapper<TransactionDTO>> makeDeposit(
            @RequestBody Map<String, Object> payload) {

        String childId = getParam(payload, "childId");
        Double amount  = getAmount(payload);
        TransactionDTO tx = parentService.faireVersement(childId, amount); // ← plus d'id père
        return ResponseEntity.status(201).body(
            Wrapper.success(201, "Deposit successfully executed.", tx)
        );
    }

    // POST /api/v1/father/withdrawals
    @PostMapping("/withdrawals")
    public ResponseEntity<Wrapper<TransactionDTO>> withdrawFromChild(
            @RequestBody Map<String, Object> payload) {

        String childId = getParam(payload, "childId");
        Double amount  = getAmount(payload);
        TransactionDTO tx = parentService.retirerArgentEnfant(childId, amount); // ← plus d'id père
        return ResponseEntity.status(201).body(
            Wrapper.success(201, "Withdrawal successfully executed.", tx)
        );
    }

    // GET /api/v1/father/transactions
    @GetMapping("/transactions")
    public ResponseEntity<Wrapper<List<TransactionDTO>>> getTransactions() {
        return ResponseEntity.ok(
            Wrapper.success(200, "Transactions retrieved successfully.",
                parentService.obtenirHistoriqueGlobal())
        );
    }

    // GET /api/v1/father/wallets
    @GetMapping("/wallets")
    public ResponseEntity<Wrapper<List<UserDTO>>> getAllWallets() {
        List<UserDTO> wallets = parentService.obtenirTousLesPortefeuilles()
            .stream()
            .map(UserDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(
            Wrapper.success(200, "Wallets retrieved successfully.", wallets)
        );
    }
}