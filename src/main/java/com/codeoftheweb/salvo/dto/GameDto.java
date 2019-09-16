package com.codeoftheweb.salvo.dto;

import java.util.Date;
import java.util.List;

public class GameDto {

    private long id;
    private Date created;
    private List<GamePlayerDto> gamePlayers;
    private List<ShipDto> ships;
    private List<ScoreDto> scores;
    private long turn;
    private List<TurnDto> turns;

    public List<TurnDto> getTurns() {
        return turns;
    }

    public void setTurns(List<TurnDto> turns) {
        this.turns = turns;
    }

    public List<ShipDto> getShips() {
        return ships;
    }

    public void setShips(List<ShipDto> ships) {
        this.ships = ships;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<GamePlayerDto> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(List<GamePlayerDto> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public List<ScoreDto> getScores() {
        return scores;
    }

    public void setScores(List<ScoreDto> scores) {
        this.scores = scores;
    }

    public long getTurn() {
        return turn;
    }

    public void setTurn(long turn) {
        this.turn = turn;
    }
}
