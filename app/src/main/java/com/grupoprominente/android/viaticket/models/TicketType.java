package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;
import com.grupoprominente.android.viaticket.R;

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
    OTHER;

    public int getResource() {
        switch(this) {
            case FOOD: return R.string.ticket_type_food;
            case TAXI: return R.string.ticket_type_taxi;
            case TRANSPORT: return R.string.ticket_type_transport;
            case LODGING: return R.string.ticket_type_lodging;
            case OTHER: return R.string.ticket_type_other;
            default: throw new IllegalArgumentException();
        }
    }

}
