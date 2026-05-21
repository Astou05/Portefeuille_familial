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

    // Route pour charger son compte : POST http://localhost:8080/api/pere/creer-monnaie
    @PostMapping("/creer-monnaie")
    public ResponseEntity<?> creerMonnaie(@RequestBody Map<String, Object> payload) {
        try {
            String pereId = (String) payload.get("pereId");
            Double montant = Double.parseDouble(payload.get("montant").toString());
            Utilisateur pereMisAJour = pereService.creerMonnaie(pereId, montant);
            return ResponseEntity.ok(pereMisAJour);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Route pour envoyer de l'argent : POST http://localhost:8080/api/pere/verser
    @PostMapping("/verser")
    public ResponseEntity<?> faireVersement(@RequestBody Map<String, Object> payload) {
        try {
            String pereId = (String) payload.get("pereId");
            String enfantId = (String) payload.get("enfantId");
            Double montant = Double.parseDouble(payload.get("montant").toString());
            Transaction t = pereService.faireVersement(pereId, enfantId, montant);
            return ResponseEntity.ok(t);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Route pour voir l'historique : GET http://localhost:8080/api/pere/historique
    @GetMapping("/historique")
    public ResponseEntity<List<Transaction>> voirHistorique() {
        return ResponseEntity.ok(pereService.obtenirHistoriqueGlobal());
    }
}
