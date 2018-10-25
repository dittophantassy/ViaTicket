package com.grupoprominente.android.viaticket.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.io.Serializable;

/**
 * Created by FCouzo on 13/7/2018.
 */

@Table
public class Ticket extends SugarRecord implements Serializable {
    private Long Id;
    private CurrencyType currency;
    private TicketType ticketType;
    private Double amount;
    private long issueDate;
    private String imageFile;
    private Expense expense;

    private static final long serialVersionUID = 5530255968065458983L;


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public long getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(long issueDate) {
        this.issueDate = issueDate;
    }

    public String getImageFile() { return imageFile; }

    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    public Expense getExpense() { return expense; }

    public void setExpense(Expense expense) { this.expense = expense; }
}
