package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
public class Turn {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private long turnNumber;
    private long yourShipsLeft;
    private long opponentShipsLeft;

    public Turn() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
