package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Date joinTime = new Date();
    private int turn;

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() {}

    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date creationTime) {
        this.joinTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoes.add(salvo);
    }

    public GamePlayer getOpponent() {

        GamePlayer opponent = new GamePlayer();
        Set<GamePlayer> gamePlayers = this.getGame().getGamePlayers();

        for (GamePlayer gamePlayer: gamePlayers) {
            if (gamePlayer.getId() != this.id) {
                opponent = gamePlayer;
            }
        }

        return opponent;
    }

    public Set<Hit> getHits(Set<Salvo> salvoes) {

        List<String> salvoLocations = new ArrayList<String>();

        for (Salvo salvo: salvoes) {
            salvoLocations = salvo.getLocations();
        }

        Set<Hit> hits = new HashSet<>();
        Set<Ship> ships = this.getShips();

        for (Ship ship: ships) {

            for (String salvoLocation: salvoLocations) {

                if (ship.getLocations().contains(salvoLocation)) {
                    Hit hit = new Hit();
                    hit.setLocation(salvoLocation);
                    hit.setShipType(ship.getType());
                    hits.add(hit);
                }
            }
        }

        return hits;
    }
}