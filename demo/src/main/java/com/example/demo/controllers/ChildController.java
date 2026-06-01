package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.objects.dtos.TransactionDTO;
import com.example.demo.objects.dtos.UserDTO;
import com.example.demo.objects.dtos.Wrapper;
import com.example.demo.services.ChildService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    // POST /api/v1/children/transfers
    @PostMapping("/transfers")
    public ResponseEntity<Wrapper<TransactionDTO>> transfer(@RequestBody Map<String, Object> payload) {
        String senderId    = (String) payload.get("senderId");
        String recipientId = (String) payload.get("recipientId");
        Double amount      = Double.parseDouble(payload.get("amount").toString());

        TransactionDTO dto = childService.effectuerTransfert(senderId, recipientId, amount);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            Wrapper.success(201, "Transfer executed successfully.", dto)
        );
    }

    // GET /api/v1/children/{id}/balance 
    @GetMapping("/{id}/balance")
    public ResponseEntity<Wrapper<UserDTO>> getBalance(@PathVariable String id) {
        UserDTO dto = new UserDTO(childService.obtenirSolde(id));
        return ResponseEntity.ok(
            Wrapper.success(200, "Child wallet balance retrieved successfully.", dto)
        );
    }

    // GET /api/v1/children/{id}/transactions  
    @GetMapping("/{id}/transactions")
    public ResponseEntity<Wrapper<List<TransactionDTO>>> getTransactions(@PathVariable String id) {
        List<TransactionDTO> history = childService.obtenirHistoriqueEnfant(id);
        return ResponseEntity.ok(
            Wrapper.success(200, "Transaction history retrieved successfully.", history)
        );
    }
}