package com.example.visiongo.database.dao;

import androidx.room.*;
import com.example.visiongo.database.entities.LuogoPreferito;
import java.util.List;

@Dao
public interface LuogoDao {

    @Insert
    long inserisci(LuogoPreferito luogo);

    @Delete
    void elimina(LuogoPreferito luogo);

    @Query("SELECT * FROM luoghi_preferiti WHERE id_utente = :idUtente")
    List<LuogoPreferito> luoghiDellUtente(int idUtente);

    @Query("SELECT * FROM luoghi_preferiti WHERE id_utente = :idUtente AND tipo = :tipo LIMIT 1")
    LuogoPreferito trovaTipo(int idUtente, String tipo);
}
