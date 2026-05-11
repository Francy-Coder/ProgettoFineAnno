package com.example.visiongo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "utenti")
public class Utente {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_utente")
    public int idUtente;

    public String nome;
    public String email;

    @ColumnInfo(name = "password_hash")
    public String passwordHash;

    @ColumnInfo(name = "usa_audio")
    public boolean usaAudio = true;

    @ColumnInfo(name = "livello_dettaglio")
    public String livelloDettaglio = "medio"; // basso / medio / alto

    @ColumnInfo(name = "evita_traffico")
    public boolean evitaTraffico = true;
}
