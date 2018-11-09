package com.grupoprominente.android.viaticket.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;
import java.util.Date;


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
    @SerializedName("CID")
    private String cid;
    @SerializedName("Imagen")
    private byte[] image;
    private static final long serialVersionUID = 5530255968065458983L;

    public Ticket() {
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
