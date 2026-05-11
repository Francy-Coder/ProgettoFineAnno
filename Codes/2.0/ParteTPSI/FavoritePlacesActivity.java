package com.example.visiongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class FavoritePlacesActivity extends AppCompatActivity {

    TextToSpeech tts;
    ListView listFavorites;
    Button btnHome, btnWork;

    // Lista simulata — in produzione viene caricata dal DB (Room / MySQL via REST)
    private final ArrayList<String> favorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout riusa activity_destination con ListView aggiunto
        setContentView(R.layout.activity_favorite_places);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Luoghi preferiti. Seleziona una destinazione.");
            }
        });

        // Luoghi di esempio (in produzione arrivano dal DB)
        favorites.add("Casa — Via Roma 1, Milano");
        favorites.add("Lavoro — Viale Monza 100, Milano");
        favorites.add("Palestra — Via Torino 50, Milano");

        listFavorites = findViewById(R.id.listFavorites);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, favorites);
        listFavorites.setAdapter(adapter);

        listFavorites.setOnItemClickListener((parent, view, position, id) -> {
            String selected = favorites.get(position);
            // Estrae solo il nome del luogo prima del " — "
            String placeName = selected.split(" — ")[0];
            speak("Avvio navigazione verso " + placeName);

            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("destination", placeName);
            startActivity(intent);
        });

        btnHome = findViewById(R.id.btnGoHome);
        btnWork = findViewById(R.id.btnGoWork);

        btnHome.setOnClickListener(v -> {
            speak("Navigazione verso casa");
            startNavigationTo("Casa");
        });
        btnWork.setOnClickListener(v -> {
            speak("Navigazione verso il lavoro");
            startNavigationTo("Lavoro");
        });
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
