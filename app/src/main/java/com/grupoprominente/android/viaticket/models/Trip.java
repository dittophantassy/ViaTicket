package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Trip {
    @SerializedName("IdViaje")
    private int idTrip;
    @SerializedName("CID")
    private String cid;
    @SerializedName("Username")
    private String username;
    @SerializedName("Fecha")
    private Date tripDate;
    @SerializedName("Destino")
    private String destination;

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTripDate() {
        return tripDate;
    }

    public void setTripDate(Date tripDate) {
        this.tripDate = tripDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
