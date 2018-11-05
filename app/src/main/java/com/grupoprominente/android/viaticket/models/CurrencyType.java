package com.grupoprominente.android.viaticket.models;

/**
 * Created by FCouzo on 13/7/2018.
 */

public enum CurrencyType {
    PESO (0), DOLLAR (1);

    private int type;

    private CurrencyType(int type){
        this.type = type;
    }
}
