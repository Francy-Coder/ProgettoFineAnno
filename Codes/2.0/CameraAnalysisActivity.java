package com.example.visiongo;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import com.example.visiongo.network.RetrofitClient;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraAnalysisActivity extends AppCompatActivity {

    PreviewView viewFinder;
    TextView txtObstacle;
    TextToSpeech tts;

    private long ultimaAnalisi = 0;
    private static final long INTERVALLO_MS = 3000; // analizza ogni 3 secondi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout da creare: activity_camera_analysis.xml
        // con PreviewView + TextView sovrapposto
        setContentView(R.layout.activity_camera_analysis);

        viewFinder  = findViewById(R.id.viewFinder);
        txtObstacle = findViewById(R.id.txtObstacle);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS)
                tts.setLanguage(Locale.ITALIAN);
        });

        avviaCameraX();
    }

    private void avviaCameraX() {
        var cameraProviderFuture =
            ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider =
                    cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build();

                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(this),
                    this::analizzaFrame);

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void analizzaFrame(ImageProxy imageProxy) {
        long ora = System.currentTimeMillis();
        if (ora - ultimaAnalisi < INTERVALLO_MS) {
            imageProxy.close();
            return;
        }
        ultimaAnalisi = ora;

        // Converti frame in JPEG base64
        ByteBuffer buffer = imageProxy.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String b64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        imageProxy.close();

        // Invia al server Python
        Map<String, String> body = new HashMap<>();
        body.put("frame", b64);

        // Nota: sostituisci con l'endpoint del tuo server Python
        // RetrofitClient supporta solo il backend Java — crea un client separato
        // per il server Python su porta 5000
        inviaAlServerPython(body);
    }

    private void inviaAlServerPython(Map<String, String> body) {
        // Chiama POST http://10.0.2.2:5000/analizza
        // e legge messaggio_vocale dalla risposta
        // Implementazione con OkHttp diretta:
        new Thread(() -> {
            try {
                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
                String json = new com.google.gson.Gson().toJson(body);

                okhttp3.Request request = new okhttp3.Request.Builder()
                    .url("http://10.0.2.2:5000/analizza")
                    .post(okhttp3.RequestBody.create(json,
                        okhttp3.MediaType.parse("application/json")))
                    .build();

                okhttp3.Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseJson = response.body().string();
                    com.google.gson.JsonObject obj =
                        new com.google.gson.JsonParser()
                            .parse(responseJson).getAsJsonObject();

                    String messaggio =
                        obj.get("messaggio_vocale").getAsString();
                    boolean sicuro =
                        obj.get("sicuro").getAsBoolean();

                    runOnUiThread(() -> {
                        txtObstacle.setText(messaggio);
                        if (!sicuro) speak(messaggio);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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