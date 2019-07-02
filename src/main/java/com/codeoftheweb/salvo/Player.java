package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;

    public Player() {}

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public Player(String userName) {
        this.userName = userName;
    }

    public String toString() {
        return userName;
    }

    public void addGamePlayer(GamePlayer gameplayer) {
        gameplayer.setPlayer(this);
        gamePlayers.add(gameplayer);
    }

    public List<Game> getGames() {
        return gamePlayers.stream().map(g -> g.getGame()).collect(toList());
    }
}
