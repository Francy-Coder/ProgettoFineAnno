package com.example.visiongo.backend.controller;

import com.example.visiongo.backend.model.CronologiaPercorso;
import com.example.visiongo.backend.repository.CronologiaRepository;
import com.example.visiongo.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cronologia")
public class CronologiaController {

    @Autowired
    private CronologiaRepository cronologiaRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // GET /api/cronologia/{idUtente}
    @GetMapping("/{idUtente}")
    public ResponseEntity<List<CronologiaPercorso>> getCronologia(
            @PathVariable int idUtente,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenValido(authHeader))
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(
            cronologiaRepository.findByIdUtenteOrderByIdCronologiaDesc(idUtente)
        );
    }

    // POST /api/cronologia — salva un percorso completato
    @PostMapping
    public ResponseEntity<CronologiaPercorso> salva(
            @RequestBody CronologiaPercorso cronologia,
            @RequestHeader("Authorization") String authHeader) {

        if (!tokenValido(authHeader))
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(cronologiaRepository.save(cronologia));
    }

    private boolean tokenValido(String header) {
        if (header == null || !header.startsWith("Bearer ")) return false;
        return jwtUtil.isTokenValido(header.substring(7));
    }
}
