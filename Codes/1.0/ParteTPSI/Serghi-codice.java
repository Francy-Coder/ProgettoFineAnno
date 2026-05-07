// activity_main.xml → Home Page
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#000000"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/txtTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="VisionGo"
        android:textSize="36sp"
        android:textStyle="bold"
        android:textColor="#FFFFFF"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="40dp"/>

    <Button
        android:id="@+id/btnVoice"
        android:layout_width="300dp"
        android:layout_height="80dp"
        android:text="Comando Vocale"
        android:textSize="24sp"
        android:backgroundTint="#1E88E5"
        android:textColor="#FFFFFF"
        android:contentDescription="Pulsante comando vocale"
        app:layout_constraintTop_toBottomOf="@id/txtTitle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="60dp"/>

    <Button
        android:id="@+id/btnDestination"
        android:layout_width="300dp"
        android:layout_height="80dp"
        android:text="Inserisci Destinazione"
        android:textSize="22sp"
        android:backgroundTint="#43A047"
        android:textColor="#FFFFFF"
        android:contentDescription="Pulsante inserisci destinazione"
        app:layout_constraintTop_toBottomOf="@id/btnVoice"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="30dp"/>

    <Button
        android:id="@+id/btnEmergency"
        android:layout_width="300dp"
        android:layout_height="80dp"
        android:text="Emergenza"
        android:textSize="22sp"
        android:backgroundTint="#E53935"
        android:textColor="#FFFFFF"
        android:contentDescription="Pulsante emergenza"
        app:layout_constraintTop_toBottomOf="@id/btnDestination"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="30dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>

// MainActivity.java
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

// activity_destination.xml
<?xml version="1.0" encoding="utf-8"?>

<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#121212">

    <TextView
        android:id="@+id/txtDest"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Inserisci Destinazione"
        android:textSize="28sp"
        android:textColor="#FFFFFF"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="50dp"/>

    <EditText
        android:id="@+id/editDestination"
        android:layout_width="320dp"
        android:layout_height="70dp"
        android:hint="Es. Stazione Centrale"
        android:textSize="22sp"
        android:backgroundTint="#FFFFFF"
        android:textColor="#FFFFFF"
        android:textColorHint="#BBBBBB"
        android:contentDescription="Campo destinazione"
        app:layout_constraintTop_toBottomOf="@id/txtDest"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="50dp"/>

    <Button
        android:id="@+id/btnStartNavigation"
        android:layout_width="300dp"
        android:layout_height="80dp"
        android:text="Avvia Navigazione"
        android:textSize="22sp"
        android:backgroundTint="#00C853"
        android:textColor="#FFFFFF"
        android:contentDescription="Avvia navigazione"
        app:layout_constraintTop_toBottomOf="@id/editDestination"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="40dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>

// DestinationActivity.java
package com.example.visiongo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class DestinationActivity extends AppCompatActivity {

    EditText editDestination;
    Button btnStartNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        editDestination =
                findViewById(R.id.editDestination);

        btnStartNavigation =
                findViewById(R.id.btnStartNavigation);

        btnStartNavigation.setOnClickListener(v -> {

            String destination =
                    editDestination.getText().toString();

            Intent intent = new Intent(
                    DestinationActivity.this,
                    NavigationActivity.class);

            intent.putExtra("destination",
                    destination);

            startActivity(intent);
        });
    }
}

// activity_navigation.xml
<?xml version="1.0" encoding="utf-8"?>

<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#000000">

    <TextView
        android:id="@+id/txtNavigation"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Navigazione Attiva"
        android:textSize="32sp"
        android:textStyle="bold"
        android:textColor="#FFFFFF"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="60dp"/>

    <TextView
        android:id="@+id/txtDirection"
        android:layout_width="300dp"
        android:layout_height="wrap_content"
        android:text="Tra 10 metri gira a destra"
        android:textSize="28sp"
        android:textColor="#00E676"
        android:gravity="center"
        app:layout_constraintTop_toBottomOf="@id/txtNavigation"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="80dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>

// NavigationActivity.java
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

// AndroidManifest.xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.visiongo">

    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.VIBRATE"/>

    <application
        android:allowBackup="true"
        android:label="VisionGo"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.DayNight">

        <activity android:name=".NavigationActivity"/>
        <activity android:name=".DestinationActivity"/>

        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name=
                    "android.intent.action.MAIN"/>

                <category android:name=
                    "android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>

    </application>

</manifest>


