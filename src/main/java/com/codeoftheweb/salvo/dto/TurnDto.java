package com.codeoftheweb.salvo.dto;

public class TurnDto {

    private long turnNumber;
    private long yourShipsLeft;
    private long opponentShipsLeft;

    public long getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(long turnNumber) {
        this.turnNumber = turnNumber;
    }

    public long getYourShipsLeft() {
        return yourShipsLeft;
    }

    public void setYourShipsLeft(long yourShipsLeft) {
        this.yourShipsLeft = yourShipsLeft;
    }

    public long getOpponentShipsLeft() {
        return opponentShipsLeft;
    }

    public void setOpponentShipsLeft(long opponentShipsLeft) {
        this.opponentShipsLeft = opponentShipsLeft;
    }
}
