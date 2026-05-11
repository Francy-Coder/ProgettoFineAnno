package com.example.visiongo.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.visiongo.database.dao.*;
import com.example.visiongo.database.entities.*;

@Database(
    entities = {
        Utente.class,
        LuogoPreferito.class,
        Percorso.class,
        CronologiaPercorso.class,
        FeedbackSicurezza.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UtenteDao utenteDao();
    public abstract LuogoDao luogoDao();
    public abstract PercorsoDao percorsoDao();

    // Singleton — un'unica istanza del DB per tutta l'app
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "visiongo_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
