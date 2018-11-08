package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by FCouzo on 13/7/2018.
 */

@Table
public class Ticket extends SugarRecord implements Serializable {
    @SerializedName("IdTicket")
    private Long id;
    @SerializedName("IdViaje")
    private int idTrip;
    @SerializedName("Moneda")
    private CurrencyType currency;
    @SerializedName("IdCategoria")
    private TicketType ticketType;
    @SerializedName("Monto")
    private Double amount;
    @SerializedName("Fecha")
    private Date issueDate;
    private String imageFile;
    private Expense expense;
    @SerializedName("CID")
    private String cid;
    @SerializedName("Imagen")
    private byte[] image;
    private static final long serialVersionUID = 5530255968065458983L;

    public Ticket() {
        String img = "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";
        image = img.getBytes();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public int getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(int idTrip) {
        this.idTrip = idTrip;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }



}
