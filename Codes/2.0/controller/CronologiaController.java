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

    @Autowired private CronologiaRepository cronologiaRepo;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/{idUtente}")
    public ResponseEntity<List<CronologiaPercorso>> get(
            @PathVariable int idUtente,
            @RequestHeader("Authorization") String auth) {

        if (!tokenOk(auth)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(
            cronologiaRepo.findByIdUtenteOrderByIdCronologiaDesc(idUtente));
    }

    @PostMapping
    public ResponseEntity<CronologiaPercorso> salva(
            @RequestBody CronologiaPercorso c,
            @RequestHeader("Authorization") String auth) {

        if (!tokenOk(auth)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(cronologiaRepo.save(c));
    }

    private boolean tokenOk(String header) {
        return header != null &&
               header.startsWith("Bearer ") &&
               jwtUtil.isTokenValido(header.substring(7));
    }
}