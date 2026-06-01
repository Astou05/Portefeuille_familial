package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.objects.daos.User;
import com.example.demo.enums.EnumRole;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByRole(EnumRole role); // ← AJOUTÉ : permet à getFather() de trouver le père par son rôle
}