package com.codeoftheweb.salvo.dto;

import com.codeoftheweb.salvo.Player;

import java.util.Date;

public class ScoreDto {

    private long playerId;
    private String playerName;
    private double result;
    private Date finishDate = new Date();

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
