package com.example.visiongo.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // In produzione: URL del server reale
    private static final String BASE_URL = "http://10.0.2.2:8080/"; // localhost da emulatore

    private static Retrofit instance;

    public static ApiService getService() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance.create(ApiService.class);
    }
}
