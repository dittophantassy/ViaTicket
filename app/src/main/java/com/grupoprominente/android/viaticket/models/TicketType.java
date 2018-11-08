package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by FCouzo on 13/7/2018.
 */

public enum TicketType {
    @SerializedName("1")
    FOOD,
    @SerializedName("2")
    TAXI,
    @SerializedName("3")
    TRANSPORT,
    @SerializedName("4")
    LODGING,
    @SerializedName("5")
    OTHER
}
