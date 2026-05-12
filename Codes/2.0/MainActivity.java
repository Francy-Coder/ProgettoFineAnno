package com.example.visiongo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnVoice, btnDestination, btnFavorites, btnHistory, btnEmergency;
    TextToSpeech tts;

    private static final int REQ_AUDIO  = 101;
    private static final int REQ_CALL   = 100;
    private static final String EMERGENCY_NUMBER = "112";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoice       = findViewById(R.id.btnVoice);
        btnDestination = findViewById(R.id.btnDestination);
        btnFavorites   = findViewById(R.id.btnFavorites);
        btnHistory     = findViewById(R.id.btnHistory);
        btnEmergency   = findViewById(R.id.btnEmergency);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Benvenuto in VisionGo. Seleziona un'opzione.");
            }
        });

        btnVoice.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQ_AUDIO);
            } else {
                startVoiceRecognition();
            }
        });

        btnDestination.setOnClickListener(v ->
            startActivity(new Intent(this, DestinationActivity.class)));

        btnFavorites.setOnClickListener(v ->
            startActivity(new Intent(this, FavoritePlacesActivity.class)));

        btnHistory.setOnClickListener(v ->
            startActivity(new Intent(this, HistoryActivity.class)));

        btnEmergency.setOnClickListener(v -> emergencyCall());
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Parla ora...");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 1 && res == RESULT_OK && data != null) {
            ArrayList<String> results =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty())
                handleVoiceCommand(results.get(0).toLowerCase());
        }
    }

    private void handleVoiceCommand(String command) {
        if (command.contains("casa")) {
            speak("Avvio navigazione verso casa.");
            startNavigationTo("Casa");
        } else if (command.contains("lavoro")) {
            speak("Avvio navigazione verso il lavoro.");
            startNavigationTo("Lavoro");
        } else if (command.contains("emergenza") || command.contains("aiuto")) {
            emergencyCall();
        } else if (command.contains("cronologia")) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (command.contains("preferiti")) {
            startActivity(new Intent(this, FavoritePlacesActivity.class));
        } else if (command.contains("vai a") || command.contains("portami a")) {
            String dest = command.replace("vai a","").replace("portami a","").trim();
            speak("Avvio navigazione verso " + dest);
            startNavigationTo(dest);
        } else {
            speak("Comando non riconosciuto. Riprova.");
            Toast.makeText(this, "Comando: " + command, Toast.LENGTH_SHORT).show();
        }
    }

    private void startNavigationTo(String destination) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }

    private void emergencyCall() {
        speak("Chiamata di emergenza in corso.");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CALL_PHONE}, REQ_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" + EMERGENCY_NUMBER));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int req, String[] perms, int[] grants) {
        super.onRequestPermissionsResult(req, perms, grants);
        if (grants.length > 0 && grants[0] == PackageManager.PERMISSION_GRANTED) {
            if (req == REQ_CALL) emergencyCall();
            if (req == REQ_AUDIO) startVoiceRecognition();
        } else {
            Toast.makeText(this, "Permesso negato", Toast.LENGTH_SHORT).show();
        }
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