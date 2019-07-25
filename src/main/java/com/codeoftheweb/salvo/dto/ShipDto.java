package com.codeoftheweb.salvo.dto;

import java.util.List;

public class ShipDto {

    private String type;
    private List<String> locations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}