package com.example.visiongo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
    tableName = "luoghi_preferiti",
    foreignKeys = @ForeignKey(
        entity = Utente.class,
        parentColumns = "id_utente",
        childColumns = "id_utente",
        onDelete = ForeignKey.CASCADE
    )
)
public class LuogoPreferito {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_luogo")
    public int idLuogo;

    @ColumnInfo(name = "id_utente")
    public int idUtente;

    public String nome;
    public String indirizzo;
    public double latitudine;
    public double longitudine;
    public String tipo; // casa / lavoro / tempo_libero / altro
}