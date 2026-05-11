package com.example.visiongo;

import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class NavigationActivity extends AppCompatActivity {

    TextView txtDirection;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        txtDirection = findViewById(R.id.txtDirection);

        String destination =
                getIntent().getStringExtra("destination");

        txtDirection.setText(
                "Percorso verso " + destination);

        tts = new TextToSpeech(this, status -> {
            if(status == TextToSpeech.SUCCESS){
                tts.setLanguage(Locale.ITALIAN);

                speak("Navigazione avviata verso "
                        + destination);
            }
        });

        Vibrator vibrator =
                (Vibrator) getSystemService(VIBRATOR_SERVICE);

        vibrator.vibrate(1000);
    }

    private void speak(String text){
        tts.speak(text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                null);
    }
}
