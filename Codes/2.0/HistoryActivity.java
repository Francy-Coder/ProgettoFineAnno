package com.example.visiongo;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.visiongo.database.AppDatabase;
import com.example.visiongo.database.entities.Percorso;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    ListView listHistory;
    TextView txtEmpty;
    Button btnBackHistory;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listHistory    = findViewById(R.id.listHistory);
        txtEmpty       = findViewById(R.id.txtEmpty);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Cronologia percorsi.");
            }
        });

        btnBackHistory.setOnClickListener(v -> finish());

        int idUtente = getSharedPreferences("visiongo", MODE_PRIVATE)
            .getInt("id_utente", 1);

        // Carica gli ultimi 20 percorsi dal DB
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Percorso> percorsi = AppDatabase
                .getInstance(this)
                .percorsoDao()
                .ultimeDestinazioni(idUtente, 20);

            runOnUiThread(() -> {
                if (percorsi.isEmpty()) {
                    txtEmpty.setVisibility(View.VISIBLE);
                    speak("Nessun percorso effettuato.");
                } else {
                    ArrayList<String> items = new ArrayList<>();
                    for (Percorso p : percorsi)
                        items.add(p.puntoArrivo + " — " +
                            p.durataMinuti + " min, " +
                            p.distanzaKm + " km");

                    listHistory.setAdapter(new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        items));
                }
            });
        });
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