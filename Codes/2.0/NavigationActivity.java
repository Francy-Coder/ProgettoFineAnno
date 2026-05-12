package com.example.visiongo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.Locale;

public class NavigationActivity extends AppCompatActivity {

    TextView txtNavigation, txtDirection, txtGpsStatus, txtDistance;
    Button btnRepeat, btnStopNav;
    TextToSpeech tts;
    LocationManager locationManager;
    Vibrator vibrator;

    private String destination;
    private String lastInstruction = "";

    private static final long MIN_TIME_MS = 3000;
    private static final float MIN_DIST_M = 5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        txtNavigation = findViewById(R.id.txtNavigation);
        txtDirection  = findViewById(R.id.txtDirection);
        txtGpsStatus  = findViewById(R.id.txtGpsStatus);
        txtDistance   = findViewById(R.id.txtDistance);
        btnRepeat     = findViewById(R.id.btnRepeat);
        btnStopNav    = findViewById(R.id.btnStopNav);

        destination = getIntent().getStringExtra("destination");
        if (destination == null || destination.isEmpty())
            destination = "destinazione sconosciuta";

        txtNavigation.setText("Verso: " + destination);
        txtDirection.setText("Acquisizione GPS...");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Navigazione avviata verso " + destination +
                      ". Attendi il segnale GPS.");
            }
        });

        btnRepeat.setOnClickListener(v -> {
            if (!lastInstruction.isEmpty())
                speak(lastInstruction);
            else
                speak("Nessuna istruzione disponibile al momento.");
        });

        btnStopNav.setOnClickListener(v -> {
            speak("Navigazione fermata.");
            if (locationManager != null)
                locationManager.removeUpdates(locationListener);
            finish();
        });

        startLocationUpdates();
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            onPositionUpdated(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            speak("GPS disattivato. Attiva la posizione.");
            txtGpsStatus.setText("GPS: disattivato");
        }

        @Override
        public void onProviderEnabled(String provider) {
            speak("GPS attivato.");
        }
    };

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permesso GPS non concesso",
                    Toast.LENGTH_LONG).show();
            speak("GPS non disponibile. Verifica i permessi.");
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_MS, MIN_DIST_M, locationListener);
            txtGpsStatus.setText("GPS: attivo");
        } else if (locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_MS, MIN_DIST_M, locationListener);
            speak("GPS non disponibile, uso la rete.");
            txtGpsStatus.setText("GPS: rete");
        } else {
            speak("Nessun provider di posizione disponibile.");
            txtGpsStatus.setText("GPS: non disponibile");
        }
    }

    private void onPositionUpdated(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float  acc = location.getAccuracy();

        // Placeholder: in produzione si calcola con Google Maps Directions API
        String istruzione = String.format(Locale.ITALIAN,
            "Posizione aggiornata. Lat %.4f, Lon %.4f. Precisione %.0f metri.",
            lat, lon, acc);

        txtDirection.setText("Verso " + destination);
        txtDistance.setText(String.format(Locale.ITALIAN,
            "Lat: %.5f  Lon: %.5f", lat, lon));
        txtGpsStatus.setText("Precisione: " + (int) acc + " m");

        lastInstruction = istruzione;
        vibrator.vibrate(150);
    }

    private void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) { tts.stop(); tts.shutdown(); }
        if (locationManager != null)
            locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }
}