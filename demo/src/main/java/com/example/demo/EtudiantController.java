package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api")
public class EtudiantController {

    private final EtudiantRepository etudiantRepository;
    public EtudiantController(EtudiantRepository etudiantRepository){
        this.etudiantRepository = etudiantRepository;
    }

    @GetMapping("/etudiants")
    public List<Etudiant> listertousLesEtudiants() {
        return etudiantRepository.findAll();
    }

    @PostMapping("/etudiants")
    public String ajouterEtudiant(@RequestBody Etudiant nouvelEtudiant) {
        etudiantRepository.save(nouvelEtudiant);
        
        return "L'étudiant " + nouvelEtudiant.getNom() + " a été ajouté avec succès";
    }
    
    


    @GetMapping("/hello")
    public String direBonjour() {
        return "Felicitation Astou, ton premier endpoint Spring boot fonctionne";
    }

    @GetMapping("/texte")
    public Etudiant monProfil() {
        return new Etudiant("Astou", "Informatique");
    }
    
    
    
    
}
