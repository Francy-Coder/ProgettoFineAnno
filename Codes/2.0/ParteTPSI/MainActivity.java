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

    Button btnVoice, btnDestination, btnEmergency, btnFavorites;
    TextToSpeech tts;

    private static final int REQUEST_CALL    = 100;
    private static final int REQUEST_AUDIO   = 101;
    private static final String EMERGENCY_NUMBER = "112"; // numero emergenza

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoice       = findViewById(R.id.btnVoice);
        btnDestination = findViewById(R.id.btnDestination);
        btnEmergency   = findViewById(R.id.btnEmergency);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Benvenuto in VisionGo. Seleziona un'opzione.");
            }
        });

        btnVoice.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO);
            } else {
                startVoiceRecognition();
            }
        });

        btnDestination.setOnClickListener(v ->
            startActivity(new Intent(this, DestinationActivity.class))
        );

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String command = result.get(0).toLowerCase();
                handleVoiceCommand(command);
            }
        }
    }

    // Interpreta i comandi vocali principali
    private void handleVoiceCommand(String command) {
        if (command.contains("casa")) {
            speak("Avvio navigazione verso casa");
            startNavigationTo("Casa");
        } else if (command.contains("lavoro")) {
            speak("Avvio navigazione verso il lavoro");
            startNavigationTo("Lavoro");
        } else if (command.contains("emergenza") || command.contains("aiuto")) {
            emergencyCall();
        } else if (command.contains("vai a") || command.contains("portami a")) {
            String dest = command.replace("vai a", "").replace("portami a", "").trim();
            speak("Avvio navigazione verso " + dest);
            startNavigationTo(dest);
        } else {
            speak("Comando non riconosciuto. Riprova.");
        }
    }

    private void startNavigationTo(String destination) {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }

    private void emergencyCall() {
        speak("Chiamata di emergenza in corso");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL,
                    Uri.parse("tel:" + EMERGENCY_NUMBER));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            emergencyCall();
        } else if (requestCode == REQUEST_AUDIO &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecognition();
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
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
