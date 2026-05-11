package com.example.visiongo.backend.controller;

import com.example.visiongo.backend.model.LuogoPreferito;
import com.example.visiongo.backend.repository.LuogoRepository;
import com.example.visiongo.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/luoghi")
public class LuoghiController {

    @Autowired
    private LuogoRepository luogoRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Recupera tutti i luoghi dell'utente
    // GET /api/luoghi/{idUtente}
    @GetMapping("/{idUtente}")
    public ResponseEntity<List<LuogoPreferito>> getLuoghi(
            @PathVariable int idUtente,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenValido(authHeader))
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(luogoRepository.findByIdUtente(idUtente));
    }

    // Aggiunge un nuovo luogo preferito
    // POST /api/luoghi
    @PostMapping
    public ResponseEntity<LuogoPreferito> aggiungi(
            @RequestBody LuogoPreferito luogo,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenValido(authHeader))
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(luogoRepository.save(luogo));
    }

    // Elimina un luogo per ID
    // DELETE /api/luoghi/{idLuogo}
    @DeleteMapping("/{idLuogo}")
    public ResponseEntity<?> elimina(
            @PathVariable int idLuogo,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenValido(authHeader))
            return ResponseEntity.status(401).build();

        luogoRepository.deleteById(idLuogo);
        return ResponseEntity.ok().build();
    }

    private boolean tokenValido(String header) {
        if (header == null || !header.startsWith("Bearer ")) return false;
        return jwtUtil.isTokenValido(header.substring(7));
    }
}
