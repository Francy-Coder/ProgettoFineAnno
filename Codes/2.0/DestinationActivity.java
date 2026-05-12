package com.example.visiongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;

public class DestinationActivity extends AppCompatActivity {

    EditText editDestination;
    Button btnStartNavigation, btnVoiceInput, btnBack;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        editDestination    = findViewById(R.id.editDestination);
        btnStartNavigation = findViewById(R.id.btnStartNavigation);
        btnVoiceInput      = findViewById(R.id.btnVoiceInput);
        btnBack            = findViewById(R.id.btnBack);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Inserisci la destinazione o usa la voce.");
            }
        });

        btnStartNavigation.setOnClickListener(v -> {
            String dest = editDestination.getText().toString().trim();
            if (dest.isEmpty()) {
                speak("Inserisci prima una destinazione.");
                Toast.makeText(this, "Inserisci una destinazione",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            startNavigationTo(dest);
        });

        btnVoiceInput.setOnClickListener(v -> startVoiceRecognition());

        btnBack.setOnClickListener(v -> finish());
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di la destinazione...");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String dest = result.get(0);
                editDestination.setText(dest);
                speak("Hai detto: " + dest + ". Premi Avvia per confermare.");
            }
        }
    }

    private void startNavigationTo(String dest) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("destination", dest);
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