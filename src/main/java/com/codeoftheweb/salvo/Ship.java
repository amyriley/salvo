package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String type;

    @ElementCollection
    @Column(name="locations")
    private List<String> locations = new ArrayList<>();

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_player_id")
    private GamePlayer gamePlayer;

    @OneToMany(mappedBy = "ship", fetch = FetchType.EAGER)
    private Set<Hit> hits = new HashSet<>();

    public Set<Hit> getHits() {
        return hits;
    }

    public void setHits(Set<Hit> hits) {
        this.hits = hits;
    }

    public Ship() {}

    public Ship (String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void addHit(Hit hit) {
        hit.setShip(this);
        hits.add(hit);
    }
}
