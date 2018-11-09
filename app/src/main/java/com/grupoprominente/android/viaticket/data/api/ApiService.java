package com.grupoprominente.android.viaticket.data.api;



import com.grupoprominente.android.viaticket.data.api.response.TicketResponse;
import com.grupoprominente.android.viaticket.data.api.response.TripResponse;
import com.grupoprominente.android.viaticket.data.api.response.UserResponse;
import com.grupoprominente.android.viaticket.models.Ticket;
import com.grupoprominente.android.viaticket.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

public interface ApiService
{
    @HTTP(method = "GET", path = "Usuarios/Login")
    Call<UserResponse> login(@Query("username") String username, @Query("password") String password);

    @HTTP(method = "GET", path = "Viajes")
    Call<TripResponse> getTripsByUsername(@Query("username") String username);

    @HTTP(method = "POST", path = "Tickets", hasBody = true)
    Call<TicketResponse> sendTickets(@Body ArrayList<Ticket> tickets);
}
