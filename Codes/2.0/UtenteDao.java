package com.example.visiongo.database.dao;

import androidx.room.*;
import com.example.visiongo.database.entities.Utente;
import java.util.List;

@Dao
public interface UtenteDao {
    @Insert
    long inserisci(Utente utente);

    @Update
    void aggiorna(Utente utente);

    @Delete
    void elimina(Utente utente);

    @Query("SELECT * FROM utenti WHERE email = :email LIMIT 1")
    Utente trovaPerEmail(String email);

    @Query("SELECT * FROM utenti WHERE id_utente = :id LIMIT 1")
    Utente trovaPerID(int id);

    @Query("SELECT * FROM utenti")
    List<Utente> tutti();
}