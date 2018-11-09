package com.grupoprominente.android.viaticket.data.api.response;

import com.google.gson.annotations.SerializedName;
import com.grupoprominente.android.viaticket.models.Ticket;

import java.util.ArrayList;

public class TicketResponse extends GenericResponse {
    @SerializedName("Tickets")
    public ArrayList<Ticket> tickets;

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }
}


