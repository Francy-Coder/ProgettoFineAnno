package com.example.visiongo.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente")
    private int idUtente;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "usa_audio")
    private boolean usaAudio = true;

    @Column(name = "livello_dettaglio")
    private String livelloDettaglio = "medio";

    @Column(name = "evita_traffico")
    private boolean evitaTraffico = true;

    public int getIdUtente()               { return idUtente; }
    public String getNome()                { return nome; }
    public void setNome(String n)          { this.nome = n; }
    public String getEmail()               { return email; }
    public void setEmail(String e)         { this.email = e; }
    public String getPasswordHash()        { return passwordHash; }
    public void setPasswordHash(String h)  { this.passwordHash = h; }
    public boolean isUsaAudio()            { return usaAudio; }
    public void setUsaAudio(boolean u)     { this.usaAudio = u; }
}