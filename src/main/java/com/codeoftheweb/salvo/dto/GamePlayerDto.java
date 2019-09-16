package com.codeoftheweb.salvo.dto;

import java.util.List;

public class GamePlayerDto {

    private long id;

    private boolean firstPlayer;

    private boolean turnToPlaceSalvoes;

    private List<SalvoDto> salvoDtos;

    public List<SalvoDto> getSalvoes() {
        return salvoDtos;
    }

    public void setSalvoes(List<SalvoDto> salvoDtos) {
        this.salvoDtos = salvoDtos;
    }

    private PlayerDto player;

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public boolean isTurnToPlaceSalvoes() {
        return turnToPlaceSalvoes;
    }

    public void setTurnToPlaceSalvoes(boolean turnToPlaceSalvoes) {
        this.turnToPlaceSalvoes = turnToPlaceSalvoes;
    }
}
