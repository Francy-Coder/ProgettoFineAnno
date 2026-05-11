package com.example.visiongo.database.dao;

import androidx.room.*;
import com.example.visiongo.database.entities.Percorso;
import com.example.visiongo.database.entities.CronologiaPercorso;
import java.util.List;

@Dao
public interface PercorsoDao {

    @Insert
    long inserisciPercorso(Percorso percorso);

    @Insert
    long inserisciCronologia(CronologiaPercorso cronologia);

    @Query("SELECT * FROM percorsi WHERE id_utente = :idUtente")
    List<Percorso> percorsiDellUtente(int idUtente);

    @Query("""
        SELECT p.* FROM percorsi p
        INNER JOIN cronologia_percorsi c ON p.id_percorso = c.id_percorso
        WHERE c.id_utente = :idUtente
        ORDER BY c.id_cronologia DESC
        LIMIT :limite
    """)
    List<Percorso> ultimeDestinazioni(int idUtente, int limite);

    @Query("SELECT * FROM cronologia_percorsi WHERE id_utente = :idUtente ORDER BY id_cronologia DESC")
    List<CronologiaPercorso> cronologiaDellUtente(int idUtente);
}
