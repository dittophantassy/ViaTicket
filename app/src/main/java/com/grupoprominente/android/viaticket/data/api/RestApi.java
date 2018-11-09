package com.grupoprominente.android.viaticket.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grupoprominente.android.viaticket.constants.ApiConstants;
import com.grupoprominente.android.viaticket.data.api.response.TicketResponse;
import com.grupoprominente.android.viaticket.data.api.response.TripResponse;
import com.grupoprominente.android.viaticket.data.api.response.UserResponse;
import com.grupoprominente.android.viaticket.models.Ticket;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApi {
    private static RestApi restApi;

    private RestApi() {
        if (restApi != null)
            throw new RuntimeException("Use getInstance() method to get the single instance of this class");
    }

    public static synchronized RestApi getInstance() {
        if (restApi == null)
            restApi = new RestApi();

        return restApi;
    }

    public UserResponse login(String email, String password) {
        UserResponse response = null;

        try
        {
            Retrofit retrofit = buildRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<UserResponse> loginCall = apiService.login(email, password);
            response = loginCall.execute().body();
        } catch (Exception e) {
        }

        return response;
    }

    public TripResponse getTripsByUsername(String username) {
        TripResponse tripResponse = null;

        try
        {
            Retrofit retrofit = buildRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<TripResponse> tripsCall = apiService.getTripsByUsername(username);
            tripResponse = tripsCall.execute().body();
        } catch (Exception e) {
        }

        return tripResponse;
    }

    public TicketResponse sendTickets(ArrayList<Ticket> tickets) {
        TicketResponse ticketResponse = null;

        try
        {
            Retrofit retrofit = buildRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);
            Call<TicketResponse> sendTickets = apiService.sendTickets(tickets);
            ticketResponse = sendTickets.execute().body();
        } catch (Exception e) {
        }

        return ticketResponse;
    }

    private Retrofit buildRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new Retrofit.Builder().
                baseUrl(ApiConstants.API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
    }
}
