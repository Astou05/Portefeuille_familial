package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pere")
public class PereController {

    @Autowired
    private PereService pereService;

    // POST /api/pere/creer-monnaie
    @PostMapping("/creer-monnaie")
    public ResponseEntity<Utilisateur> creerMonnaie(@RequestBody Map<String, Object> payload) {
        String pereId  = (String) payload.get("pereId");
        Double montant = Double.parseDouble(payload.get("montant").toString());
        return ResponseEntity.status(201).body(pereService.creerMonnaie(pereId, montant));
    }

    // POST /api/pere/verser
    @PostMapping("/verser")
    public ResponseEntity<TransactionDTO> faireVersement(@RequestBody Map<String, Object> payload) {
        String pereId   = (String) payload.get("pereId");
        String enfantId = (String) payload.get("enfantId");
        Double montant  = Double.parseDouble(payload.get("montant").toString());
        return ResponseEntity.status(201).body(pereService.faireVersement(pereId, enfantId, montant));
    }

    // POST /api/pere/retirer
    @PostMapping("/retirer")
    public ResponseEntity<TransactionDTO> retirerArgentEnfant(@RequestBody Map<String, Object> payload) {
        String pereId   = (String) payload.get("pereId");
        String enfantId = (String) payload.get("enfantId");
        Double montant  = Double.parseDouble(payload.get("montant").toString());
        return ResponseEntity.status(201).body(pereService.retirerArgentEnfant(pereId, enfantId, montant));
    }

    // GET /api/pere/historique
    @GetMapping("/historique")
    public ResponseEntity<List<TransactionDTO>> voirHistorique() {
        return ResponseEntity.ok(pereService.obtenirHistoriqueGlobal());
    }

    // GET /api/pere/portefeuilles
    @GetMapping("/portefeuilles")
    public ResponseEntity<List<Utilisateur>> voirPortefeuilles() {
        return ResponseEntity.ok(pereService.obtenirTousLesPortefeuilles());
    }
}