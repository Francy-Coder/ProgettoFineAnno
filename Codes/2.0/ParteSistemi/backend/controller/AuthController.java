package com.example.visiongo.backend.controller;

import com.example.visiongo.backend.model.Utente;
import com.example.visiongo.backend.repository.UtenteRepository;
import com.example.visiongo.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // POST /api/auth/registrazione
    @PostMapping("/registrazione")
    public ResponseEntity<?> registra(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nome = body.get("nome");

        if (utenteRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("errore", "Email già registrata"));
        }

        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setEmail(email);
        utente.setPasswordHash(encoder.encode(password));
        utenteRepository.save(utente);

        String token = jwtUtil.generaToken(email);
        return ResponseEntity.ok(Map.of("token", token, "messaggio", "Registrazione avvenuta"));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        Optional<Utente> opt = utenteRepository.findByEmail(email);
        if (opt.isEmpty() || !encoder.matches(password, opt.get().getPasswordHash())) {
            return ResponseEntity.status(401)
                    .body(Map.of("errore", "Credenziali non valide"));
        }

        String token = jwtUtil.generaToken(email);
        return ResponseEntity.ok(Map.of(
                "token", token,
                "nome", opt.get().getNome(),
                "idUtente", opt.get().getIdUtente()
        ));
    }
}
