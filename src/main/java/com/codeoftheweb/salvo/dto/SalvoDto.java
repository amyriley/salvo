package com.codeoftheweb.salvo.dto;

import java.util.List;

public class SalvoDto {

    private int turn;
    private List<String> locations;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
