package com.codeoftheweb.salvo.dto;

import java.util.List;

public class SalvoDto {

    private List<String> locations;
    private List<HitDto> hits;

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<HitDto> getHits() {
        return hits;
    }

    public void setHits(List<HitDto> hits) {
        this.hits = hits;
    }
}
