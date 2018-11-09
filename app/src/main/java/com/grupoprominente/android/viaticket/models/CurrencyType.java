package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;
import com.grupoprominente.android.viaticket.R;


public enum CurrencyType {
    @SerializedName("1")
    PESO,
    @SerializedName("2")
    DOLLAR;

    public int getResource() {
        switch(this) {
            case PESO: return R.string.currency_peso;
            case DOLLAR: return R.string.currency_dollar;
            default: throw new IllegalArgumentException();
        }
    }
}

