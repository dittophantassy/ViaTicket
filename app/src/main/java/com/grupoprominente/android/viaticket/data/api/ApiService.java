package com.grupoprominente.android.viaticket.data.api;



import com.grupoprominente.android.viaticket.models.User;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

public interface ApiService
{
    @HTTP(method = "GET", path = "Usuarios/Login")
    Call<User> login(@Query("username") String username, @Query("password") String password);

//    @HTTP(method = "POST", path = "Usuario", hasBody = true)
//    Call<GenericResponse> signUp(@Body HashMap<String, String> user);
//
//    @HTTP(method = "POST", path = "Usuario/RecuperarPassword", hasBody = true)
//    Call<GenericResponse> recoverPassword(@Body HashMap<String, String> user);
//
//    @HTTP(method = "POST", path = "Usuario/ActualizarPassword", hasBody = true)
//    Call<GenericResponse> changePassword(@Body HashMap<String, String> user, @Query("nuevaPass") String newPassword, @Query("confirmarPass") String confirmNewPassword);
//
//    @HTTP(method = "GET", path = "EventoBasico/GetEmpresasEventoBasico")
//    Call<ArrayList<Sponsor>> getSponsors();


//    @HTTP(method = "GET", path = "Iniciativa")
//    Call<ArrayList<Initiative>> getInitiatives();

}
