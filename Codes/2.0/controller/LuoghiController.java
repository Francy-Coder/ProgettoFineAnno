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

    @Autowired private LuogoRepository luogoRepo;
    @Autowired private JwtUtil jwtUtil;

    @GetMapping("/{idUtente}")
    public ResponseEntity<List<LuogoPreferito>> getLuoghi(
            @PathVariable int idUtente,
            @RequestHeader("Authorization") String auth) {

        if (!tokenOk(auth)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(luogoRepo.findByIdUtente(idUtente));
    }

    @PostMapping
    public ResponseEntity<LuogoPreferito> aggiungi(
            @RequestBody LuogoPreferito luogo,
            @RequestHeader("Authorization") String auth) {

        if (!tokenOk(auth)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(luogoRepo.save(luogo));
    }

    @DeleteMapping("/{idLuogo}")
    public ResponseEntity<?> elimina(
            @PathVariable int idLuogo,
            @RequestHeader("Authorization") String auth) {

        if (!tokenOk(auth)) return ResponseEntity.status(401).build();
        luogoRepo.deleteById(idLuogo);
        return ResponseEntity.ok().build();
    }

    private boolean tokenOk(String header) {
        return header != null &&
               header.startsWith("Bearer ") &&
               jwtUtil.isTokenValido(header.substring(7));
    }
}