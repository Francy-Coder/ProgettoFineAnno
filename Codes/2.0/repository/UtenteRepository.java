package com.example.visiongo.backend.repository;

import com.example.visiongo.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Optional<Utente> findByEmail(String email);
    boolean existsByEmail(String email);
}