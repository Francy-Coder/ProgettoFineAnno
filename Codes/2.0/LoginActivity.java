package com.example.visiongo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.visiongo.network.RetrofitClient;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin, btnGoRegister;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail    = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Accedi a VisionGo. Inserisci email e password.");
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String pass  = editPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                speak("Inserisci email e password.");
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("password", pass);

            RetrofitClient.getService().login(body)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call,
                                           Response<Map<String, Object>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Map<String, Object> data = response.body();
                            String token = (String) data.get("token");
                            int idUtente = ((Double) data.get("idUtente")).intValue();

                            // Salva token e id in SharedPreferences
                            SharedPreferences prefs =
                                getSharedPreferences("visiongo", MODE_PRIVATE);
                            prefs.edit()
                                .putString("jwt", "Bearer " + token)
                                .putInt("id_utente", idUtente)
                                .apply();

                            speak("Accesso effettuato. Benvenuto.");
                            startActivity(new Intent(
                                LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            speak("Credenziali non valide. Riprova.");
                            Toast.makeText(LoginActivity.this,
                                "Credenziali errate", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call,
                                          Throwable t) {
                        speak("Impossibile contattare il server.");
                        Toast.makeText(LoginActivity.this,
                            "Errore di rete: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    }
                });
        });

        btnGoRegister.setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class)));
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