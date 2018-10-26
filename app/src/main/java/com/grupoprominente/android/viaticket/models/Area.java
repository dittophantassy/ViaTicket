package com.grupoprominente.android.viaticket.models;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Area extends SugarRecord implements Serializable {
    private Long Id;
    private String name;
    private Long nomenclature;

    @Override
    public Long getId() {
        return Id;
    }

    @Override
    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNomenclature() {
        return nomenclature;
    }

    public void setNomenclature(Long nomenclature) {
        this.nomenclature = nomenclature;
    }

    public Area getParentArea() {
        return parentArea;
    }

    public void setParentArea(Area parentArea) {
        this.parentArea = parentArea;
    }

    private Area parentArea;
}
