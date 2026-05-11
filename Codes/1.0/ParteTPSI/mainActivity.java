package com.example.visiongo;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btnVoice, btnDestination, btnEmergency;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVoice = findViewById(R.id.btnVoice);
        btnDestination = findViewById(R.id.btnDestination);
        btnEmergency = findViewById(R.id.btnEmergency);

        tts = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS){
                tts.setLanguage(Locale.ITALIAN);
            }
        });

        btnVoice.setOnClickListener(v -> startVoiceRecognition());

        btnDestination.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    DestinationActivity.class);
            startActivity(intent);
        });

        btnEmergency.setOnClickListener(v -> {
            speak("Chiamata di emergenza attivata");
            Toast.makeText(this,
                    "Emergenza attivata",
                    Toast.LENGTH_LONG).show();
        });
    }

    private void startVoiceRecognition() {

        Intent intent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "it-IT");

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if(requestCode == 1 &&
                resultCode == RESULT_OK &&
                data != null){

            ArrayList<String> result =
                    data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);

            String command = result.get(0);

            speak("Hai detto " + command);
        }
    }

    private void speak(String text){
        tts.speak(text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                null);
    }
}
