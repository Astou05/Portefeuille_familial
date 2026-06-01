package com.example.demo.controllers;

import com.example.demo.objects.dtos.PagedResponse;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.objects.dtos.UserDTO;
import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ParentService;
import com.example.demo.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/father")
public class ParentController {

    @Autowired
    private ParentService parentService;

    private String getParam(Map<String, Object> payload, String key) {
        if (!payload.containsKey(key) || payload.get(key) == null)
            throw new AppException(400, "Missing required parameter: " + key);
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
        UserDTO dto = new UserDTO(parentService.creerMonnaie(amount));
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
        TransactionDTO tx = parentService.faireVersement(childId, amount);
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
        TransactionDTO tx = parentService.retirerArgentEnfant(childId, amount);
        return ResponseEntity.status(201).body(
            Wrapper.success(201, "Withdrawal successfully executed.", tx)
        );
    }

    // GET /api/v1/father/transactions?page=0&size=10
    // ← MODIFIÉ : ajout de @RequestParam page et size
    @GetMapping("/transactions")
    public ResponseEntity<Wrapper<PagedResponse<TransactionDTO>>> getTransactions(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<TransactionDTO> result = parentService.obtenirHistoriqueGlobal(pageable);
        return ResponseEntity.ok(
            Wrapper.success(200, "Transactions retrieved successfully.", result)
        );
    }

    // GET /api/v1/father/wallets?page=0&size=10
    // ← MODIFIÉ : ajout de @RequestParam page et size
    @GetMapping("/wallets")
    public ResponseEntity<Wrapper<PagedResponse<UserDTO>>> getAllWallets(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<UserDTO> result = parentService.obtenirTousLesPortefeuilles(pageable);
        return ResponseEntity.ok(
            Wrapper.success(200, "Wallets retrieved successfully.", result)
        );
    }
}