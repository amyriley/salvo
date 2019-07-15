package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.Game;
import com.codeoftheweb.salvo.Player;
import com.codeoftheweb.salvo.Salvo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GamePlayerDto {

    private long id;

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
}
