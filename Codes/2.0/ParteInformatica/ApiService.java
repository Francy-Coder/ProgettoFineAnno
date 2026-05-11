package com.example.visiongo.network;

import com.example.visiongo.database.entities.LuogoPreferito;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("api/auth/login")
    Call<Map<String, Object>> login(@Body Map<String, String> body);

    @POST("api/auth/registrazione")
    Call<Map<String, Object>> registra(@Body Map<String, String> body);

    @GET("api/luoghi/{idUtente}")
    Call<List<LuogoPreferito>> getLuoghi(
            @Path("idUtente") int idUtente,
            @Header("Authorization") String token
    );

    @POST("api/luoghi")
    Call<LuogoPreferito> aggiungiLuogo(
            @Body LuogoPreferito luogo,
            @Header("Authorization") String token
    );

    @GET("api/cronologia/{idUtente}")
    Call<List<Map<String, Object>>> getCronologia(
            @Path("idUtente") int idUtente,
            @Header("Authorization") String token
    );
}
