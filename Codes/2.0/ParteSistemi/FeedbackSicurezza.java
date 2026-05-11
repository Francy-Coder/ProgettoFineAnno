package com.example.visiongo.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
    tableName = "feedback_sicurezza",
    foreignKeys = {
        @ForeignKey(
            entity = Utente.class,
            parentColumns = "id_utente",
            childColumns = "id_utente",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Percorso.class,
            parentColumns = "id_percorso",
            childColumns = "id_percorso",
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class FeedbackSicurezza {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_feedback")
    public int idFeedback;

    @ColumnInfo(name = "id_utente")
    public int idUtente;

    @ColumnInfo(name = "id_percorso")
    public int idPercorso;

    public String descrizione;
    public String tipo;    // ostacolo / lavori / pericolo / altro
    public int gravita;    // 1-5
}
