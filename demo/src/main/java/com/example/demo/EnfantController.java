package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enfant")
public class EnfantController {

    @Autowired
    private EnfantService enfantService;

    // POST /api/enfant/transfert
    @PostMapping("/transfert")
    public ResponseEntity<TransactionDTO> effectuerTransfert(@RequestBody Map<String, Object> payload) {
        String emetteurId     = (String) payload.get("emetteurId");
        String destinataireId = (String) payload.get("destinataireId");
        Double montant        = Double.parseDouble(payload.get("montant").toString());
        return ResponseEntity.status(201).body(
            enfantService.effectuerTransfert(emetteurId, destinataireId, montant)
        );
    }

    // GET /api/enfant/solde/{id}
    @GetMapping("/solde/{id}")
    public ResponseEntity<Utilisateur> voirSolde(@PathVariable String id) {
        return ResponseEntity.ok(enfantService.obtenirSolde(id));
    }

    // GET /api/enfant/historique/{id}
    @GetMapping("/historique/{id}")
    public ResponseEntity<List<TransactionDTO>> voirHistorique(@PathVariable String id) {
        return ResponseEntity.ok(enfantService.obtenirHistoriqueEnfant(id));
    }
}