package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    // ✅ Ajout : trouver tous les enfants (utile pour consulter tous les portefeuilles)
    List<Utilisateur> findByRole(EnumRole role);
}