package com.grupoprominente.android.viaticket.models;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Expense extends SugarRecord implements Serializable {
    private Long Id;
    private String name;
    private String description;
    private boolean submitted = false;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
