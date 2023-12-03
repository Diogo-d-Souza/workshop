package com.technical.workshop.model.DTO;

import com.technical.workshop.model.User;

public class CarOwnerDTO {
    private String id;
    private String name;

    public CarOwnerDTO() {
    }

    public CarOwnerDTO(User user) {
        id = user.getId();
        name = user.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
