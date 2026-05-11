package com.example.visiongo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
    tableName = "percorsi",
    foreignKeys = @ForeignKey(
        entity = Utente.class,
        parentColumns = "id_utente",
        childColumns = "id_utente",
        onDelete = ForeignKey.CASCADE
    )
)
public class Percorso {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_percorso")
    public int idPercorso;

    @ColumnInfo(name = "id_utente")
    public int idUtente;

    public String nome;

    @ColumnInfo(name = "punto_partenza")
    public String puntoPartenza;

    @ColumnInfo(name = "punto_arrivo")
    public String puntoArrivo;

    @ColumnInfo(name = "distanza_km")
    public float distanzaKm;

    @ColumnInfo(name = "durata_minuti")
    public int durataMinuti;

    @ColumnInfo(name = "livello_sicurezza")
    public String livelloSicurezza; // basso / medio / alto

    @ColumnInfo(name = "presenza_semafori")
    public boolean presenzaSemafori;

    @ColumnInfo(name = "presenza_marciapiedi")
    public boolean presenzaMarciapiedi;
}
