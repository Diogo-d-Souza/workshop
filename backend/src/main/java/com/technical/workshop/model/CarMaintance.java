package com.technical.workshop.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CarMaintance {
    @Id
    private String id;
    private String maintanceName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaintanceName() {
        return maintanceName;
    }

    public void setMaintanceName(String maintanceName) {
        this.maintanceName = maintanceName;
    }
}
