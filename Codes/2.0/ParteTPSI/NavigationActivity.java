package com.example.visiongo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.Locale;

public class NavigationActivity extends AppCompatActivity {

    TextView txtDirection, txtNavigation;
    TextToSpeech tts;
    LocationManager locationManager;
    Vibrator vibrator;

    private String destination;
    private Location lastLocation;
    private static final long MIN_TIME_MS   = 3000;  // aggiorna ogni 3 secondi
    private static final float MIN_DIST_M   = 5f;    // aggiorna ogni 5 metri

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        txtNavigation = findViewById(R.id.txtNavigation);
        txtDirection  = findViewById(R.id.txtDirection);

        destination = getIntent().getStringExtra("destination");
        if (destination == null || destination.isEmpty()) {
            destination = "destinazione sconosciuta";
        }

        txtNavigation.setText("Navigazione verso:\n" + destination);
        txtDirection.setText("Acquisizione posizione GPS...");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Navigazione avviata verso " + destination +
                      ". Attendi l'acquisizione del segnale GPS.");
            }
        });

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,
                    "Permesso GPS non concesso", Toast.LENGTH_LONG).show();
            speak("GPS non disponibile. Verifica i permessi.");
            return;
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = location;
                onPositionUpdated(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
                speak("GPS disattivato. Attiva la posizione nel dispositivo.");
                txtDirection.setText("GPS non disponibile");
            }

            @Override
            public void onProviderEnabled(String provider) {
                speak("GPS attivato.");
            }
        };

        // Prova GPS hardware, poi rete come fallback
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_MS, MIN_DIST_M, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_MS, MIN_DIST_M, locationListener);
            speak("GPS non disponibile, uso la rete.");
        } else {
            speak("Nessun provider di posizione disponibile.");
            txtDirection.setText("GPS non disponibile");
        }
    }

    // Aggiorna UI e voce quando la posizione cambia
    private void onPositionUpdated(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        float accuracy = location.getAccuracy();

        // Placeholder: in un'app reale qui si calcolerebbero le istruzioni
        // reali tramite Google Maps Directions API
        String info = String.format(Locale.ITALIAN,
                "Lat: %.5f\nLon: %.5f\nPrecisione: %.1f m",
                lat, lon, accuracy);
        txtDirection.setText("Verso " + destination + "\n" + info);

        // Vibrazione di conferma aggiornamento posizione
        vibrator.vibrate(150);
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
        if (locationManager != null)
            locationManager.removeUpdates(location -> {});
        super.onDestroy();
    }
}
