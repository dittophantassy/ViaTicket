package com.grupoprominente.android.viaticket.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.grupoprominente.android.viaticket.models.Trip;

import java.util.ArrayList;

public class TripResponse extends GenericResponse {
    @SerializedName("Viajes")
    public ArrayList<Trip> trips;

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }
}
