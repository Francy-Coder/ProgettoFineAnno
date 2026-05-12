package com.example.visiongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.visiongo.database.AppDatabase;
import com.example.visiongo.database.entities.LuogoPreferito;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class FavoritePlacesActivity extends AppCompatActivity {

    TextToSpeech tts;
    ListView listFavorites;
    Button btnGoHome, btnGoWork, btnAddPlace;

    private final List<String> displayList = new ArrayList<>();
    private final List<LuogoPreferito> luoghiList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);

        btnGoHome    = findViewById(R.id.btnGoHome);
        btnGoWork    = findViewById(R.id.btnGoWork);
        btnAddPlace  = findViewById(R.id.btnAddPlace);
        listFavorites = findViewById(R.id.listFavorites);

        adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, displayList);
        listFavorites.setAdapter(adapter);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Luoghi preferiti. Seleziona una destinazione.");
            }
        });

        // Carica luoghi dal DB Room in background
        int idUtente = getSharedPreferences("visiongo", MODE_PRIVATE)
            .getInt("id_utente", 1);

        Executors.newSingleThreadExecutor().execute(() -> {
            List<LuogoPreferito> luoghi = AppDatabase
                .getInstance(this)
                .luogoDao()
                .luoghiDellUtente(idUtente);

            runOnUiThread(() -> {
                luoghiList.clear();
                displayList.clear();
                for (LuogoPreferito l : luoghi) {
                    luoghiList.add(l);
                    displayList.add(l.nome + " — " + l.indirizzo);
                }
                adapter.notifyDataSetChanged();
            });
        });

        listFavorites.setOnItemClickListener((parent, view, pos, id) -> {
            LuogoPreferito luogo = luoghiList.get(pos);
            speak("Avvio navigazione verso " + luogo.nome);
            startNavigationTo(luogo.nome);
        });

        btnGoHome.setOnClickListener(v -> {
            speak("Navigazione verso casa.");
            startNavigationTo("Casa");
        });

        btnGoWork.setOnClickListener(v -> {
            speak("Navigazione verso il lavoro.");
            startNavigationTo("Lavoro");
        });

        btnAddPlace.setOnClickListener(v ->
            Toast.makeText(this, "Funzione in arrivo", Toast.LENGTH_SHORT).show()
        );
    }

    private void startNavigationTo(String destination) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }

    private void speak(String text) {
        if (tts != null)
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        super.onDestroy();
    }
}