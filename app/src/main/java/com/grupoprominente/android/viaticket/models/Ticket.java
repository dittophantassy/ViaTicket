package com.grupoprominente.android.viaticket.models;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by FCouzo on 13/7/2018.
 */

public class Ticket extends SugarRecord implements Serializable {
    private long Id;
    private CurrencyType currency;
    private TicketType ticketType;
    private Float amount;
    private long DateTime;

    public Long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public long getDateTime() {
        return DateTime;
    }

    public void setDateTime(long dateTime) {
        DateTime = dateTime;
    }
}
