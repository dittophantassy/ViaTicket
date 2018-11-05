package com.grupoprominente.android.viaticket.models;

/**
 * Created by FCouzo on 13/7/2018.
 */

public enum TicketType {
    FOOD (0), TAXI (1), TRANSPORT (2), OTHER (3);

    private int type;

    private TicketType(int type){
        this.type = type;
    }
}
