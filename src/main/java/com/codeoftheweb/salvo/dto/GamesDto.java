package com.codeoftheweb.salvo.dto;

import java.util.List;

public class GamesDto {

    private CurrentPlayerDto currentPlayer;
    private List<GameDto> games;

    public CurrentPlayerDto getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(CurrentPlayerDto currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<GameDto> getGames() {
        return games;
    }

    public void setGames(List<GameDto> games) {
        this.games = games;
    }
}
