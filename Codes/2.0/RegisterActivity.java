package com.example.visiongo;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    EditText editName, editEmail, editPassword;
    Button btnRegister, btnGoLogin;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName     = findViewById(R.id.editName);
        editEmail    = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister  = findViewById(R.id.btnRegister);
        btnGoLogin   = findViewById(R.id.btnGoLogin);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ITALIAN);
                speak("Crea il tuo account VisionGo.");
            }
        });

        btnRegister.setOnClickListener(v -> {
            String nome  = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String pass  = editPassword.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                speak("Compila tutti i campi.");
                return;
            }

            Map<String, String> body = new HashMap<>();
            body.put("nome", nome);
            body.put("email", email);
            body.put("password", pass);

            RetrofitClient.getService().registra(body)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call,
                                           Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            speak("Registrazione avvenuta. Accedi ora.");
                            Toast.makeText(RegisterActivity.this,
                                "Registrato! Accedi.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(
                                RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            speak("Errore durante la registrazione.");
                            Toast.makeText(RegisterActivity.this,
                                "Email gia usata", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call,
                                          Throwable t) {
                        speak("Impossibile contattare il server.");
                        Toast.makeText(RegisterActivity.this,
                            "Errore: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                    }
                });
        });

        btnGoLogin.setOnClickListener(v -> finish());
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