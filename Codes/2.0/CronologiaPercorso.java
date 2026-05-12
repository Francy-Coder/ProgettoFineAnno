package com.example.visiongo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
    tableName = "cronologia_percorsi",
    foreignKeys = {
        @ForeignKey(entity = Utente.class,
            parentColumns = "id_utente",
            childColumns = "id_utente",
            onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Percorso.class,
            parentColumns = "id_percorso",
            childColumns = "id_percorso",
            onDelete = ForeignKey.CASCADE)
    }
)
public class CronologiaPercorso {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_cronologia")
    public int idCronologia;

    @ColumnInfo(name = "id_utente")
    public int idUtente;

    @ColumnInfo(name = "id_percorso")
    public int idPercorso;

    public boolean completato = true;

    @ColumnInfo(name = "tempo_effettivo")
    public int tempoEffettivo;

    @ColumnInfo(name = "difficolta_percepita")
    public int difficoltaPercepita; // 1-5
}