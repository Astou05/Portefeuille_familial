package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.objects.dtos.PagedResponse;
import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.objects.dtos.UserDTO;
import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ChildService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    // POST /api/v1/children/transfers
    // ← INCHANGÉ
    @PostMapping("/transfers")
    public ResponseEntity<Wrapper<TransactionDTO>> transfer(
            @RequestBody Map<String, Object> payload) {

        String senderId    = (String) payload.get("senderId");
        String recipientId = (String) payload.get("recipientId");
        Double amount      = Double.parseDouble(payload.get("amount").toString());

        TransactionDTO dto = childService.effectuerTransfert(senderId, recipientId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Wrapper.success(201, "Transfer executed successfully.", dto)
        );
    }

    // GET /api/v1/children/{id}/balance
    // ← INCHANGÉ
    @GetMapping("/{id}/balance")
    public ResponseEntity<Wrapper<UserDTO>> getBalance(@PathVariable String id) {
        UserDTO dto = new UserDTO(childService.obtenirSolde(id));
        return ResponseEntity.ok(
            Wrapper.success(200, "Child wallet balance retrieved successfully.", dto)
        );
    }

    // GET /api/v1/children/{id}/transactions?page=0&size=10
    // ← MODIFIÉ : ajout de @RequestParam page et size
    @GetMapping("/{id}/transactions")
    public ResponseEntity<Wrapper<PagedResponse<TransactionDTO>>> getTransactions(
            @PathVariable String id,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PagedResponse<TransactionDTO> result =
            childService.obtenirHistoriqueEnfant(id, pageable);
        return ResponseEntity.ok(
            Wrapper.success(200, "Transaction history retrieved successfully.", result)
        );
    }
}